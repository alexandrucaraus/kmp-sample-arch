package eu.caraus.kmp.notes.presentation.details

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import eu.caraus.kmp.notes.domain.Note
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class NoteDetailsRoute(val noteId: String)

fun NavController.openNoteDetails(note: Note) =
    navigate(NoteDetailsRoute(noteId = note.id))

fun NavController.createNoteDetails() =
    navigate(NoteDetailsRoute(noteId = Note.NO_ID))

fun NavGraphBuilder.noteDetailRoute(
    close: () -> Unit
) = composable<NoteDetailsRoute> { params ->
    val viewModel = koinViewModel<NoteDetailsViewModel> {
        parametersOf(params.toRoute<NoteDetailsRoute>().noteId)
    }
    val noteState by viewModel.state.collectAsStateWithLifecycle()
    NoteDetailsScreen(
        note = noteState,
        close = close
    )
}
