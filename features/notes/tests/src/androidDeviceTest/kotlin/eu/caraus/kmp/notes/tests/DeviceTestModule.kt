package eu.caraus.kmp.notes.tests

import eu.caraus.kmp.database.DatabaseDiModule
import eu.caraus.kmp.notes.data.NoteDataDiModule
import eu.caraus.kmp.notes.domain.NoteDiModule
import eu.caraus.kmp.notes.ui.NoteUiDiModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module
import org.koin.ksp.generated.*

object DeviceTestModule {
    val module =
        module {
            includes(DatabaseDiModule().module)
            includes(NoteDiModule().module)
            includes(NoteUiDiModule().module)
            includes(NoteDataDiModule().module)
            includes(
                module {
                    factory { CoroutineScope(Dispatchers.Main.immediate + SupervisorJob()) }
                },
            )
        }
}
