package eu.caraus.kmp.samplearch

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun App() = Theme {
    Surface {
        Navigation(navController = rememberNavController())
    }
}
