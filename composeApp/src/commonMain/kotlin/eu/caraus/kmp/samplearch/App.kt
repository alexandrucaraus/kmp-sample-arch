package eu.caraus.kmp.samplearch

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.KoinApplication
import org.koin.dsl.KoinAppDeclaration

@Composable
fun App(koinAppDeclaration: KoinAppDeclaration) =
    KoinApplication(application = koinAppDeclaration) {
        Theme {
            Surface {
                Navigation(navController = rememberNavController())
            }
        }
    }
