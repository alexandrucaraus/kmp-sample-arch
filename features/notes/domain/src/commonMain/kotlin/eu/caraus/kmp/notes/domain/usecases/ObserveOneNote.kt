package eu.caraus.kmp.notes.domain.usecases

import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.NoteId
import eu.caraus.kmp.notes.domain.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class ObserveOneNote(
    private val repository: NoteRepository,
) {
    operator fun invoke(noteId: NoteId): Flow<Note> =
        flowOf(noteId)
            .map { repository.findById(noteId) ?: Note(id = noteId) }
}
