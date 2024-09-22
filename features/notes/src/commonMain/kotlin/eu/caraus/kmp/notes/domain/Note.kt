package eu.caraus.kmp.notes.domain

typealias NoteId = String

data class Note(
    val id: NoteId,
    val title: String = "",
    val content: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
) {
    companion object {
        const val NO_ID = ""
    }
}