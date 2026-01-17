package eu.caraus.kmp.samplearch

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import eu.caraus.kmp.notes.ui.list.NoteListRoute
import eu.caraus.kmp.notes.ui.navigation.NotesNavGraph
import eu.caraus.kmp.notes.ui.navigation.NotesSerializerModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

private val savedStateConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            // include here any other navigation serializer modules
            include(NotesSerializerModule)
        }
    }
}

@Composable
internal fun AppNavigation() {
    val backStack = rememberNavBackStack(
        configuration = savedStateConfig,
        NoteListRoute
    )
    NavDisplay(
        modifier = Modifier.fillMaxSize(),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            NotesNavGraph(key, backStack)
                ?: error("Destination not found $key")
        }
    )
}
