package eu.caraus.kmp.notes.presentation.list

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.caraus.kmp.notes.NoteFactory
import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.viewModelOf
import kotlinx.serialization.Serializable

@Serializable
data object NoteListRoute

fun NavGraphBuilder.noteListRoute(
    openNote: (Note) -> Unit,
    createNote: () -> Unit,
) = composable<NoteListRoute> {
    viewModelOf<NoteListViewModel>(NoteFactory::createNoteListViewModel)
        .state
        .collectAsStateWithLifecycle()
        .value
        .let { notesState ->
            NoteListScreen(
                state = notesState,
                openNote = openNote,
                createNote = createNote,
            )
        }
}