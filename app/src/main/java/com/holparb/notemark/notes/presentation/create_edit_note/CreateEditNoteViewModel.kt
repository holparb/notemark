package com.holparb.notemark.notes.presentation.create_edit_note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.holparb.notemark.app.navigation.NavigationRoute
import com.holparb.notemark.core.domain.result.onError
import com.holparb.notemark.core.domain.result.onSuccess
import com.holparb.notemark.notes.domain.models.Note
import com.holparb.notemark.notes.domain.repository.NoteRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

class CreateEditNoteViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val route = savedStateHandle.toRoute<NavigationRoute.CreateEditNote>()
    private val noteId = route.noteId
    private var originalNote: Note? = null
    private var isOriginalNoteNew = false

    private val _state = MutableStateFlow(CreateEditNoteState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                if(noteId.isNotBlank()) {
                    loadNote(noteId)
                }
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CreateEditNoteState()
        )

    private val _events = Channel<CreateEditNoteEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: CreateEditNoteAction) {
        when (action) {
            is CreateEditNoteAction.NoteContentChanged -> noteContentChanged(action.text)
            is CreateEditNoteAction.NoteTitleChanged -> noteTitleChanged(action.text)
            CreateEditNoteAction.CancelNote -> cancelNote()
            CreateEditNoteAction.DismissCancelDialog -> setCancelDialogVisibility(false)
            CreateEditNoteAction.SaveNoteClicked -> saveNote()
            CreateEditNoteAction.NoteCancelConfirmed -> noteCancelled()
        }
    }

    private fun setCancelDialogVisibility(visible: Boolean) {
        _state.update {
            it.copy(cancelDialogVisible = visible)
        }
    }

    private fun saveNote() {
        if(state.value.title.isBlank()) {
            return
        }
        viewModelScope.launch {
            if(isOriginalNoteNew) {
                val note = Note(
                    noteId = noteId,
                    title = state.value.title,
                    content = state.value.content,
                    createdAt = Instant.now(),
                    lastEditedAt = Instant.now()
                )
                noteRepository.createNote(note)
                    .onError { error ->
                        _events.send(CreateEditNoteEvent.Error(error))
                    }
                    .onSuccess {
                        _events.send(CreateEditNoteEvent.CreateUpdateSuccess)
                    }
            } else {
                val note = Note(
                    noteId = noteId,
                    title = state.value.title,
                    content = state.value.content,
                    createdAt = originalNote!!.createdAt,
                    lastEditedAt = Instant.now()
                )
                noteRepository.updateNote(note)
                    .onError { error ->
                        _events.send(CreateEditNoteEvent.Error(error))
                    }
                    .onSuccess {
                        _events.send(CreateEditNoteEvent.CreateUpdateSuccess)
                    }
            }
        }
    }

    private fun cancelNote() {
        if(originalNote == null) {
            viewModelScope.launch {
                _events.send(CreateEditNoteEvent.CancelSuccess)
                return@launch
            }
        }

        if(originalNote?.title != state.value.title || originalNote?.content != state.value.content) {
            setCancelDialogVisibility(true)
        } else if(isOriginalNoteNew) {
                viewModelScope.launch {
                    noteRepository.deleteNoteFromDatabase(originalNote!!.noteId)
                        .onError { error ->
                            _events.send(CreateEditNoteEvent.Error(error))
                        }
                        .onSuccess {
                            _events.send(CreateEditNoteEvent.CancelSuccess)
                        }
                }
        } else {
            viewModelScope.launch {
                _events.send(CreateEditNoteEvent.CancelSuccess)
            }
        }
    }

    private fun noteCancelled() {
        setCancelDialogVisibility(false)
        if(isOriginalNoteNew) {
            viewModelScope.launch {
                noteRepository.deleteNoteFromDatabase(originalNote!!.noteId)
                    .onError { error ->
                        _events.send(CreateEditNoteEvent.Error(error))
                    }
                    .onSuccess {
                        _events.send(CreateEditNoteEvent.CancelSuccess)
                    }
            }
        } else {
            viewModelScope.launch {
                _events.send(CreateEditNoteEvent.CancelSuccess)
            }
        }
    }

    private fun noteTitleChanged(text: String) {
        _state.update {
            it.copy(title = text)
        }
    }

    private fun noteContentChanged(text: String) {
        _state.update {
            it.copy(content = text)
        }
    }

    private fun loadNote(noteId: String) {
        viewModelScope.launch {
            noteRepository.getNote(noteId)
                .onError { error ->
                    _events.send(CreateEditNoteEvent.Error(error))
                }
                .onSuccess { note ->
                    originalNote = note
                    isOriginalNoteNew = note.title.isBlank() && note.content.isBlank()
                    _state.update {
                        it.copy(
                            title = note.title,
                            content = note.content
                        )
                    }
                }
        }
    }
}