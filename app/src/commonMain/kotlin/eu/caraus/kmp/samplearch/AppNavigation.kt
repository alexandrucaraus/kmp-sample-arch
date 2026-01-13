package eu.caraus.kmp.samplearch

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import eu.caraus.kmp.notes.ui.list.NoteListRoute
import eu.caraus.kmp.notes.ui.navigation.notesNavGraph

// Todo: move to navigation module
@Composable
internal fun AppNavigation(navController: NavHostController) = NavHost(
    modifier = Modifier.fillMaxSize(),
    navController = navController,
    startDestination = NoteListRoute,
) {
    notesNavGraph(navController)
}
