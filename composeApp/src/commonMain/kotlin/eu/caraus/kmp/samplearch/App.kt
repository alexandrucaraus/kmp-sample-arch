package eu.caraus.kmp.samplearch

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.KoinContext

@Composable
fun ComposeApp() =
    KoinContext {
        Theme {
            Surface {
                Navigation(navController = rememberNavController())
            }
        }
    }
