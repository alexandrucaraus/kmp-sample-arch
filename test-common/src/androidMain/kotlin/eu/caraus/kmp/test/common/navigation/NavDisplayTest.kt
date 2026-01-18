package eu.caraus.kmp.test.common.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule

@Composable
fun NavDisplayTest(
    startDestination: NavKey,
    serializerModule: SerializersModule,
    guest: (NavKey, NavBackStack<NavKey>) -> NavEntry<NavKey>?,
    modifier: Modifier = Modifier,
) {
    val savedStateConfig =
        SavedStateConfiguration {
            serializersModule =
                SerializersModule {
                    include(serializerModule)
                }
        }
    val backStack =
        rememberNavBackStack(
            configuration = savedStateConfig,
            startDestination,
        )
    NavDisplay(
        modifier = modifier.fillMaxSize(),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            guest(key, backStack) ?: error("Destination nav key found $key")
        },
    )
}
