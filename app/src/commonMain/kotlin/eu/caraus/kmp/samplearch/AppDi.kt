package eu.caraus.kmp.samplearch

import eu.caraus.kmp.database.DatabaseDiModule
import eu.caraus.kmp.notes.data.NoteDataDiModule
import eu.caraus.kmp.notes.domain.NoteDiModule
import eu.caraus.kmp.notes.ui.NoteUiDiModule
import org.koin.core.annotation.KoinApplication

// TODO: The modules section can be removed when the following bug is fixed
// otherwise need to indicate all modules here
// https://github.com/InsertKoinIO/koin-annotations/issues/317
@KoinApplication
(
    modules = [
        DatabaseDiModule::class,
        NoteDataDiModule::class,
        NoteDiModule::class,
        NoteUiDiModule::class
    ]
)
object AppDi
