package com.holparb.notemark.notes.presentation.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.notemark.core.domain.result.onError
import com.holparb.notemark.core.domain.result.onSuccess
import com.holparb.notemark.core.domain.user_preferences.UserPreferences
import com.holparb.notemark.notes.domain.repository.NoteRepository
import com.holparb.notemark.notes.presentation.mappers.toNoteUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteListViewModel(
    private val userPreferences: UserPreferences,
    private val noteRepository: NoteRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(NoteListState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                deriveUserInitials()
                loadInitialNotes()
                observeNotes()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = NoteListState()
        )

    fun onAction(action: NoteListAction) {
        when (action) {
            NoteListAction.CreateNoteClick -> Unit
            is NoteListAction.NoteClick -> noteClick(action.noteId)
            is NoteListAction.NoteLongClick -> noteLongClick(action.noteId)
        }
    }

    private fun noteLongClick(noteId: String) {
        println("noteLongClick")
    }

    private fun noteClick(noteId: String) {
        println("noteClick")
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
                //TODO send error event to UI
            }
            .onSuccess { notesFlow ->
                notesFlow
                    .distinctUntilChanged()
                    .onEach { notes ->
                        _state.update { state ->
                            state.copy(notes.map { it.toNoteUi() })
                        }
                    }
                    .launchIn(viewModelScope)
            }
    }
}