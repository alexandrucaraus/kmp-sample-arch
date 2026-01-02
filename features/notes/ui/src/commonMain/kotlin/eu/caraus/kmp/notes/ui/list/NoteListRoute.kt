package eu.caraus.kmp.notes.presentation.list

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.caraus.kmp.notes.domain.Note
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object NoteListRoute

fun NavGraphBuilder.noteListRoute(
    openNote: (Note) -> Unit,
    createNote: () -> Unit,
) = composable<NoteListRoute> {

    val viewModel = koinViewModel<NoteListViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    NoteListScreen(
        state = state,
        openNote = openNote,
        createNote = createNote,
    )
}
