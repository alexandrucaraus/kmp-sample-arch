package eu.caraus.kmp.notes.ui.list

import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.NoteId
import eu.caraus.kmp.notes.domain.NoteRepository
import kotlinx.coroutines.flow.Flow

open class NoteRepositoryMock: NoteRepository {
    override suspend fun save(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(notes: List<Note>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(noteId: NoteId) {
        TODO("Not yet implemented")
    }

    override suspend fun findById(noteId: NoteId): Note? {
        TODO("Not yet implemented")
    }

    override suspend fun findAll(): List<Note> {
        TODO("Not yet implemented")
    }

    override fun allAsFlow(): Flow<List<Note>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }
}
