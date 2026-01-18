package eu.caraus.kmp.notes.test

import eu.caraus.kmp.database.DatabaseDiModule
import eu.caraus.kmp.notes.data.NoteDataDiModule
import eu.caraus.kmp.notes.domain.NoteDiModule
import eu.caraus.kmp.notes.ui.NoteUiDiModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(
    includes = [
        DatabaseDiModule::class,
        NoteDiModule::class,
        NoteUiDiModule::class,
        NoteDataDiModule::class,
    ],
)
@ComponentScan("eu.caraus.kmp.notes")
class NoteDeviceTestModule {
    @Single
    fun instrumentedTestCoroutineScope() = CoroutineScope(Dispatchers.Main)
}
