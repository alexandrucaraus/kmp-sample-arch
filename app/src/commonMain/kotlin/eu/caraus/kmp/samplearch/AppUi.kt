package eu.caraus.kmp.samplearch

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppUi() = AppTheme {
    AppNavigation(navController = rememberNavController())
}
