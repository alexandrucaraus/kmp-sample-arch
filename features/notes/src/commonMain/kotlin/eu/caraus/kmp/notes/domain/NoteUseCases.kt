package eu.caraus.kmp.notes.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GetNotesListUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> =
        repository.allAsFlow().map { list -> list.sortedByDescending { it.updatedAt } }
}

class GetNoteUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(noteId: NoteId): Flow<Note> =
        flowOf(noteId).map { repository.findById(noteId) ?: Note(id = noteId) }
}

class SaveNoteUseCase(
    private val updateNote: UpdateNoteUseCase,
    private val createNote: CreateNoteUseCase,
) {
    suspend operator fun invoke(
        noteId: NoteId,
        title: String,
        content: String,
    ) {
        if (noteId == Note.NO_ID) {
            if (title.isNotBlank() && content.isNotBlank()) {
                createNote(
                    title = title,
                    content = content,
                )
            }
        } else {
            updateNote(
                id = noteId,
                title = title,
                content = content,
            )
        }
    }
}


class CreateNoteUseCase(
    private val repository: NoteRepository
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(
        title: String,
        content: String,
    ) {
        val time = Clock.System.now().toEpochMilliseconds()
        repository.save(
            Note(
                id = Uuid.random().toString(),
                title = title,
                content = content,
                createdAt = time,
                updatedAt = time,
            )
        )
    }
}

class UpdateNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(
        id: NoteId,
        title: String,
        content: String,
    ) {
        repository.findById(noteId = id)?.let { note ->
            repository.save(
                note.copy(
                    title = title,
                    content = content,
                    updatedAt = Clock.System.now().toEpochMilliseconds(),
                )
            )
        } ?: throw IllegalStateException("Note not found")
    }
}

class DeleteNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: NoteId) {
        repository.deleteById(noteId = noteId)
    }

    suspend operator fun invoke(note: Note) {
        repository.delete(note = note)
    }

    suspend operator fun invoke(notes: List<Note>) {
        repository.delete(notes = notes)
    }
}
