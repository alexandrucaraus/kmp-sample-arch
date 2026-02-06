package eu.caraus.kmp.notes.domain.usecases

import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class ObserveNotesList(
    private val repository: NoteRepository,
) {
    operator fun invoke(): Flow<List<Note>> =
        repository
            .allAsFlow()
            .map { list -> list.sortedByDescending { it.updatedAt } }
}
