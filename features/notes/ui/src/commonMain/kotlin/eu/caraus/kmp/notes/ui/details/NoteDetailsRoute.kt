package eu.caraus.kmp.notes.ui.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class NoteDetailsRoute(val noteId: String): NavKey

@Composable
fun NoteDetailRoute(
    params: NoteDetailsRoute,
    close: () -> Unit
) {
    val state by koinViewModel<NoteDetailsViewModel>(key = params.noteId) {
        parametersOf(params.noteId)
    }.state
        .collectAsStateWithLifecycle()

    NoteDetailsScreen(
        note = state,
        close = close
    )
}
