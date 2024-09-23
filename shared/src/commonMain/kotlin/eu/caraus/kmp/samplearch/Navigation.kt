package eu.caraus.kmp.samplearch

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import eu.caraus.kmp.notes.presentation.details.createNoteDetails
import eu.caraus.kmp.notes.presentation.details.noteDetailRoute
import eu.caraus.kmp.notes.presentation.details.openNoteDetails
import eu.caraus.kmp.notes.presentation.list.NoteListRoute
import eu.caraus.kmp.notes.presentation.list.noteListRoute

@Composable
internal fun Navigation(navController: NavHostController) = NavHost(
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
