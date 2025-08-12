package com.holparb.notemark.notes.presentation.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.notemark.core.domain.user_preferences.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class NoteListViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(NoteListState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                deriveUserInitials()
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
            NoteListAction.CreateNoteClick -> createNote()
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

    private fun createNote() {
        println("createNote")
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

}