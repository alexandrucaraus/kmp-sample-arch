package eu.caraus.kmp.notes.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlin.properties.Delegates

open class NoteRepositoryInMem(
    initialList: List<Note> = emptyList()
) : NoteRepository {

    private var list: List<Note> by Delegates.observable(initialList) { _, _, new ->
        listFlow.update { new }
    }

    val listFlow = MutableStateFlow(list)

    override suspend fun save(note: Note) {
        list = list + note
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

    override fun allAsFlow(): Flow<List<Note>> = listFlow

    override suspend fun deleteAll() {
        list = emptyList()
    }
}
