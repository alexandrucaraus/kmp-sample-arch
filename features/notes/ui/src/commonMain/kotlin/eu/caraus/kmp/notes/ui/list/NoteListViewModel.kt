package eu.caraus.kmp.notes.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.caraus.kmp.notes.domain.DeleteNoteUseCase
import eu.caraus.kmp.notes.domain.GetNotesListUseCase
import eu.caraus.kmp.notes.domain.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class NoteListViewModel(
    private val notesList: GetNotesListUseCase,
    private val deleteUseCase: DeleteNoteUseCase,
    scope: CoroutineScope,
) : ViewModel(scope) {

    private val notesState = MutableStateFlow(
        NoteListState(
            toggleSelection = ::toggleNoteSelection,
            deleteSelected = ::deleteSelectedNotes,
            clearSelected = ::clearSelectedNotes,
        )
    )
    val state = notesState.asStateFlow()
    private fun state() = state.value

    init {
        notesList()
            .distinctUntilChanged()
            .onEach { notes -> notesState.update { it.copy(notes = notes) } }
        .launchIn(viewModelScope)
    }

    private fun toggleNoteSelection(note: Note) {
        val updateOp : (Iterable<Note>, Note) -> List<Note> =
            if (state().selectedNotes.contains(note))
                Iterable<Note>::minus else Iterable<Note>::plus

        notesState.update { it.copy(selectedNotes = updateOp(it.selectedNotes, note)) }
    }

    private fun deleteSelectedNotes() {
        viewModelScope.launch {
            deleteUseCase(notesState.value.selectedNotes)
            clearSelectedNotes()
        }
    }

    private fun clearSelectedNotes() {
        notesState.update { it.copy(selectedNotes = emptyList()) }
    }
}