package com.holparb.notemark.notes.presentation.note_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.holparb.notemark.app.navigation.NavigationRoute
import com.holparb.notemark.core.domain.result.onError
import com.holparb.notemark.core.domain.result.onSuccess
import com.holparb.notemark.core.domain.user_preferences.UserPreferences
import com.holparb.notemark.notes.domain.repository.NoteRepository
import com.holparb.notemark.notes.presentation.mappers.toNoteUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class NoteListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val userPreferences: UserPreferences,
    private val noteRepository: NoteRepository
) : ViewModel() {

    private var hasLoadedInitialData = false
    private val route = savedStateHandle.toRoute<NavigationRoute.NoteList>()
    private val navigationFromLogin = route.navigateFromLogin
    private var selectedNoteId: String? = null

    private val _state = MutableStateFlow(NoteListState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                deriveUserInitials()
                if(navigationFromLogin) {
                    loadInitialNotes()
                }
                observeNotes()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = NoteListState()
        )

    private val _events = Channel<NoteListEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: NoteListAction) {
        when (action) {
            NoteListAction.CreateNoteClick -> createNote()
            is NoteListAction.NoteClick -> Unit
            is NoteListAction.NoteLongClick -> noteLongClick(action.noteId)
            is NoteListAction.DeleteConfirm -> deleteNote()
            NoteListAction.DeleteDialogDismiss -> dismissDialog()
        }
    }

    private fun deleteNote() {
        if(selectedNoteId == null) {
            return
        }
        viewModelScope.launch {
            noteRepository.deleteNote(selectedNoteId!!)
                .onError { error ->
                    _events.send(NoteListEvent.NoteListError(error))
                }
                .onSuccess {
                    selectedNoteId = null
                    _state.update {
                        it.copy(
                            showNoteDeleteDialog = false
                        )
                    }
                    _events.send(NoteListEvent.NoteDeleted)
                }
        }
    }

    private fun createNote() {
        viewModelScope.launch {
            noteRepository.createNewNoteInDatabase()
                .onError { error ->
                    _events.send(NoteListEvent.NoteListError(error))
                }
                .onSuccess { noteId ->
                    _events.send(NoteListEvent.NoteCreated(noteId))
                }
        }
    }

    private fun noteLongClick(noteId: String) {
        selectedNoteId = noteId
        _state.update {
            it.copy(
                showNoteDeleteDialog = true
            )
        }
    }

    private fun dismissDialog() {
        selectedNoteId = null
        _state.update {
            it.copy(
                showNoteDeleteDialog = false
            )
        }
    }

    private suspend fun deriveUserInitials() {
        val username = userPreferences.getUsername()
        val firstLetter: String
        val secondLetter: String
        val delimiter = if(username.contains('.')) {
            "."
        } else {
            " "
        }
        val splitByDelimiter = username.split(delimiter)
        when(splitByDelimiter.size) {
            1 -> {
                firstLetter = splitByDelimiter.first()[0].toString()
                secondLetter = splitByDelimiter.first()[1].toString()
            }
            2 -> {
                firstLetter = splitByDelimiter[0][0].toString()
                secondLetter = splitByDelimiter[1][0].toString()
            }
            else ->{
                firstLetter = splitByDelimiter[0][0].toString()
                secondLetter = splitByDelimiter[splitByDelimiter.size - 1][0].toString()
            }
        }
        _state.update {
            it.copy(
                userInitials = "$firstLetter$secondLetter".uppercase()
            )
        }
    }

    private fun loadInitialNotes() {
        _state.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            noteRepository.getNotes()
                .onError {
                    _state.update {
                        it.copy(isLoading = false)
                    }
                    //TODO send error event to UI
                }
                .onSuccess { notes ->
                    _state.update { state ->
                        state.copy(
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun observeNotes() {
        noteRepository.observeNotes()
            .onError {
                Timber.e("Error while observing notes")
            }
            .onSuccess { notesFlow ->
                notesFlow
                    .distinctUntilChanged()
                    .onEach { notes ->
                        Timber.d("Notes updated: $notes")
                        _state.update { state ->
                            state.copy(notes = notes.map { it.toNoteUi() })
                        }
                    }
                    .launchIn(viewModelScope)
            }
    }
}