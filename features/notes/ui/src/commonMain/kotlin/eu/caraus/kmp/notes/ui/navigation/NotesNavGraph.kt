package eu.caraus.kmp.notes.ui.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.ui.details.NoteDetailRoute
import eu.caraus.kmp.notes.ui.details.NoteDetailsRoute
import eu.caraus.kmp.notes.ui.list.NoteListRoute
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Suppress("VariableName")
val NotesSerializerModule =
    SerializersModule {
        polymorphic(NavKey::class) {
            subclass(NoteListRoute::class, NoteListRoute.serializer())
            subclass(NoteDetailsRoute::class, NoteDetailsRoute.serializer())
        }
    }

@Suppress("FunctionName")
fun NotesNavGraph(
    key: NavKey,
    backStack: NavBackStack<NavKey>,
): NavEntry<NavKey>? =
    when (key) {
        is NoteListRoute -> {
            NavEntry(key) {
                NoteListRoute(
                    openNote = { note ->
                        backStack.add(NoteDetailsRoute(note.id))
                    },
                    createNote = {
                        backStack.add(NoteDetailsRoute(Note.NO_ID))
                    },
                )
            }
        }

        is NoteDetailsRoute -> {
            NavEntry(key) { params ->
                NoteDetailRoute(
                    params = params as NoteDetailsRoute,
                    close = {
                        backStack.remove(params)
                    },
                )
            }
        }

        else -> {
            null
        }
    }
