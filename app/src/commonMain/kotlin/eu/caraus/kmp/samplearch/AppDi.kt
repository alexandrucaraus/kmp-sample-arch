package eu.caraus.kmp.samplearch

import eu.caraus.kmp.database.DatabaseDiModule
import eu.caraus.kmp.notes.NoteDataDiModule
import eu.caraus.kmp.notes.domain.NoteDiModule
import eu.caraus.kmp.notes.ui.NoteUiDiModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@ComponentScan("eu.caraus")
@Module(
    includes = [
        DatabaseDiModule::class,
        NoteDataDiModule::class,
        NoteUiDiModule::class,
        NoteDiModule::class,
    ]
)
class AppDi
