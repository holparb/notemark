package notes.presentation.note_list

data class NoteListState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)