package eu.caraus.kmp.notes.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.caraus.kmp.notes.domain.DeleteNoteUseCase
import eu.caraus.kmp.notes.domain.GetNoteUseCase
import eu.caraus.kmp.notes.domain.SaveNoteUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class NoteDetailsViewModel(
    private val noteId: String,
    getNote: GetNoteUseCase,
    private val saveNote: SaveNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
    coroutineScope: CoroutineScope,
) : ViewModel(coroutineScope) {

    private val noteState = MutableStateFlow(
        NoteState(
            updateTitle = ::updateTitle,
            updateContent = ::updateContent,
            leave = ::saveWithAction,
            delete = ::deleteWithAction,
        )
    )

    val state: StateFlow<NoteState> = noteState

    init {
        getNote(noteId = noteId).onEach { note ->
            noteState.update {
                it.copy(
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

    private fun saveWithAction(action: () -> Unit) = viewModelScope.launch {
        with(state.value) {
            saveNote(noteId = noteId, title = title, content = content)
        }
        action()
    }

    private fun deleteWithAction(action: () -> Unit) = viewModelScope.launch {
        deleteNote(noteId = noteId)
        action()
    }
}
