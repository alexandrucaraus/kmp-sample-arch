package eu.caraus.kmp.notes.data

import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.NoteId
import eu.caraus.kmp.notes.domain.NoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

@Factory(binds = [NoteRepository::class])
class NoteRepositoryRoom(
    private val noteDao: NoteDao,
) : NoteRepository {
    override suspend fun save(note: Note) = noteDao.insert(note.toDto())

    override suspend fun delete(notes: List<Note>) =
        notes
            .map(Note::toDto)
            .let { noteDao.delete(it) }

    override suspend fun deleteById(noteId: NoteId) = noteDao.deleteById(noteId)

    override suspend fun findById(noteId: NoteId): Note? = noteDao.findById(noteId)?.let(NoteDto::toEntity)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun allAsFlow(): Flow<List<Note>> =
        noteDao
            .getAllAsFlow()
            .flatMapLatest { list -> flowOf(list.map(NoteDto::toEntity)) }

    override suspend fun deleteAll() = noteDao.deleteAll()
}

private fun Note.toDto() =
    NoteDto(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

private fun NoteDto.toEntity() =
    Note(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
