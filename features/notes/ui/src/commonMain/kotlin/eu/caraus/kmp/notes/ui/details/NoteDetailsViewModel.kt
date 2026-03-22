package eu.caraus.kmp.notes.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.caraus.kmp.notes.domain.usecases.DeleteNoteUseCase
import eu.caraus.kmp.notes.domain.usecases.ObserveOneNote
import eu.caraus.kmp.notes.domain.usecases.SaveNoteUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class NoteDetailsViewModel(
    @InjectedParam
    private val noteId: String,
    observeOneNote: ObserveOneNote,
    private val saveNote: SaveNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
    coroutineScope: CoroutineScope,
) : ViewModel(coroutineScope) {
    private val noteState =
        MutableStateFlow(
            NoteState(
                updateTitle = ::updateTitle,
                updateContent = ::updateContent,
                leave = ::saveWithAction,
                delete = ::deleteWithAction,
            ),
        )

    val state: StateFlow<NoteState> = noteState

    init {
        observeOneNote(noteId = noteId)
            .onEach { note ->
                noteState.update {
                    it.copy(
                        id = note.id,
                        title = note.title,
                        content = note.content,
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun updateTitle(title: String) {
        noteState.update { it.copy(title = title) }
    }

    private fun updateContent(content: String) {
        noteState.update { it.copy(content = content) }
    }

    private fun saveWithAction(action: () -> Unit) =
        viewModelScope.launch {
            with(state.value) {
                saveNote(noteId = noteId, title = title, content = content)
            }
            action()
        }

    private fun deleteWithAction(action: () -> Unit) =
        viewModelScope.launch {
            deleteNote(noteId = noteId)
            action()
        }
}
