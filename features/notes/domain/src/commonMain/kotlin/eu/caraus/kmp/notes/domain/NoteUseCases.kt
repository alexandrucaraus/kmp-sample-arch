package eu.caraus.kmp.notes.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Factory
class ObserveNotesList(
    private val repository: NoteRepository,
) {
    operator fun invoke(): Flow<List<Note>> =
        repository
            .allAsFlow()
            .map { list -> list.sortedByDescending { it.updatedAt } }
}

@Factory
class ObserveOneNote(
    private val repository: NoteRepository,
) {
    operator fun invoke(noteId: NoteId): Flow<Note> =
        flowOf(noteId)
            .map { repository.findById(noteId) ?: Note(id = noteId) }
}

@Factory
class SaveNoteUseCase(
    repository: NoteRepository,
) {
    private val updateNote: UpdateNoteUseCase = UpdateNoteUseCase(repository)
    private val createNote: CreateNoteUseCase = CreateNoteUseCase(repository)

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

internal class CreateNoteUseCase(
    private val repository: NoteRepository,
) {
    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    suspend operator fun invoke(
        title: String,
        content: String,
    ) {
        val time =
            kotlin.time.Clock.System
                .now()
                .toEpochMilliseconds()
        repository.save(
            Note(
                id = Uuid.random().toString(),
                title = title,
                content = content,
                createdAt = time,
                updatedAt = time,
            ),
        )
    }
}

// Todo remove time dependency
internal class UpdateNoteUseCase(
    private val repository: NoteRepository,
) {
    @OptIn(ExperimentalTime::class)
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
                    updatedAt =
                        kotlin.time.Clock.System
                            .now()
                            .toEpochMilliseconds(),
                ),
            )
        } ?: error("Note not found id=$id")
    }
}

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
