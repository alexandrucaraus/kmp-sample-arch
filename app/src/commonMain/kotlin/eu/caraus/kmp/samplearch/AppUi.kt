package eu.caraus.kmp.samplearch

import androidx.compose.runtime.Composable

@Composable
fun AppUi() =
    AppTheme {
        println("Testing")
        AppNavigation()
    }
