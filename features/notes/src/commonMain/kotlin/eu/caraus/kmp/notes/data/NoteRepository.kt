package eu.caraus.kmp.notes.data

import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.NoteId
import eu.caraus.kmp.notes.domain.NoteRepository
import eu.caraus.kmp.room.NoteDao
import eu.caraus.kmp.room.NoteDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Factory
import kotlin.properties.Delegates

@Factory(binds = [NoteRepository::class])
class NoteRepositoryRoom(
    private val noteDao: NoteDao
) : NoteRepository {

    override suspend fun save(note: Note) = noteDao.insert(note.toDto())
    override suspend fun delete(note: Note) = noteDao.delete(note.toDto())
    override suspend fun delete(notes: List<Note>) =
        notes.map(Note::toDto).toTypedArray().let { noteDao.delete(*it) }

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


class NoteRepositoryInMem() : NoteRepository {

    var list: List<Note> by Delegates.observable(emptyList<Note>()) { prop, old, new ->
        listFlow.update { new }
    }

    val listFlow = MutableStateFlow<List<Note>>(list)

    override suspend fun save(note: Note) {
        list = list + note
    }

    override suspend fun delete(note: Note) {
        list = list - note
    }

    override suspend fun delete(notes: List<Note>) {
        list = list - notes
    }

    override suspend fun deleteById(noteId: NoteId) {
        list = list.filter { it.id != noteId }
    }

    override suspend fun findById(noteId: NoteId): Note? {
        return list.find { it.id == noteId }
    }

    override suspend fun findAll(): List<Note> {
        return list
    }

    override fun allAsFlow(): Flow<List<Note>> = listFlow

}
