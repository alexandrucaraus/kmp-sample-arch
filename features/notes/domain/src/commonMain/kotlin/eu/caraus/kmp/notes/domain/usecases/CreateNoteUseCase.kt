package eu.caraus.kmp.notes.domain.usecases

import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.NoteId
import eu.caraus.kmp.notes.domain.NoteRepository
import org.koin.core.annotation.Factory
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
            if (note.title == title && note.content == content) return@let
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
