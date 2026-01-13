package eu.caraus.kmp.test.common.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun NavHostTest(
    startDestination: Any,
    guest: NavGraphBuilder.(NavHostController) -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = startDestination,
    ) {
        guest(navController)
    }
}
