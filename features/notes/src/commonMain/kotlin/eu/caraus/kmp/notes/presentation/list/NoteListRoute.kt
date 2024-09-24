package eu.caraus.kmp.notes.presentation.list

import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
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

    val lifecycle = LocalLifecycleOwner.current.lifecycle.currentState
    val viewModel = koinViewModel<NoteListViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // workaround: when app is rotated, room stops sending updates
    if (lifecycle == Lifecycle.State.RESUMED) viewModel.observeNotes()

    NoteListScreen(
        state = state,
        openNote = openNote,
        createNote = createNote,
    )
}
