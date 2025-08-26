package com.holparb.notemark.notes.presentation.create_edit_note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.holparb.notemark.app.navigation.NavigationRoute
import com.holparb.notemark.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class CreateEditNoteViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val route = savedStateHandle.toRoute<NavigationRoute.CreateEditNote>()
    private val noteId = route.noteId

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

    fun onAction(action: CreateEditNoteAction) {
        when (action) {
            is CreateEditNoteAction.NoteContentChanged -> noteContentChanged(action.text)
            is CreateEditNoteAction.NoteTitleChanged -> noteTitleChanged(action.text)
            CreateEditNoteAction.CancelClicked -> cancelNote()
            CreateEditNoteAction.SaveNoteClicked -> saveNote()
        }
    }

    private fun saveNote() {

    }

    private fun cancelNote() {

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

    }
}