package eu.caraus.kmp.notes.domain

import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun save(note: Note)
    suspend fun delete(note: Note)
    suspend fun delete(notes: List<Note>)
    suspend fun deleteById(noteId: NoteId)
    suspend fun findById(noteId: NoteId): Note?
    suspend fun findAll(): List<Note>
    fun allAsFlow(): Flow<List<Note>>
}