package eu.caraus.kmp.notes.domain.usecases

import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.NoteId
import eu.caraus.kmp.notes.domain.NoteRepository
import org.koin.core.annotation.Factory

@Factory
class DeleteNoteUseCase(
    private val repository: NoteRepository,
) {
    suspend operator fun invoke(noteId: NoteId) {
        repository.deleteById(noteId = noteId)
    }

    suspend operator fun invoke(notes: List<Note>) {
        repository.delete(notes = notes)
    }
}

