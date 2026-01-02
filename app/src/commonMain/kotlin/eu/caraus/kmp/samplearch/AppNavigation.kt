package eu.caraus.kmp.samplearch

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import eu.caraus.kmp.notes.ui.details.createNoteDetails
import eu.caraus.kmp.notes.ui.details.noteDetailRoute
import eu.caraus.kmp.notes.ui.details.openNoteDetails
import eu.caraus.kmp.notes.ui.list.NoteListRoute
import eu.caraus.kmp.notes.ui.list.noteListRoute

// Todo: move to navigation module
@Composable
internal fun AppNavigation(navController: NavHostController) = NavHost(
    modifier = Modifier.fillMaxSize(),
    navController = navController,
    startDestination = NoteListRoute,
) {
    noteListRoute(
        openNote = navController::openNoteDetails,
        createNote = navController::createNoteDetails,
    )

    noteDetailRoute(
        close = navController::popBackStack
    )
}
