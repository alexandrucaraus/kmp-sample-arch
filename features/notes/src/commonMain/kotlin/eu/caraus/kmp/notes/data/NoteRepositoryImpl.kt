package eu.caraus.kmp.notes.data

import eu.caraus.kmp.database.NoteDao
import eu.caraus.kmp.database.NoteDto
import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.NoteId
import eu.caraus.kmp.notes.domain.NoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class NoteRepositoryRoom(
    private val noteDao: NoteDao
) : NoteRepository {

    override suspend fun save(note: Note) = noteDao.insert(note.toDto())
    override suspend fun delete(note: Note) = noteDao.delete(note.toDto())
    override suspend fun delete(notes: List<Note>) = notes.map(Note::toDto).toTypedArray().let { noteDao.delete(*it) }

    override suspend fun deleteById(noteId: NoteId) = noteDao.deleteById(noteId)

    override suspend fun findById(noteId: NoteId): Note? =
        noteDao.findById(noteId)?.let(NoteDto::toEntity)

    override suspend fun findAll(): List<Note> =
        noteDao.getAllAsFlow().first().map(NoteDto::toEntity)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun allAsFlow(): Flow<List<Note>> = noteDao
        .getAllAsFlow()
        .flatMapLatest { list -> flowOf(list.map(NoteDto::toEntity)) }
}

private fun Note.toDto() = NoteDto(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

private fun NoteDto.toEntity() = Note(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
