package eu.caraus.kmp.notes.ui

import eu.caraus.kmp.notes.domain.NoteDiModule
import eu.caraus.kmp.notes.ui.NoteUiDiModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module

@Module(
    includes = [
        NoteDiModule::class,
        NoteUiDiModule::class,
    ],
)
@ComponentScan("eu.caraus.kmp.notes")
class NoteHostTestModule

fun commonTestModules() =
    module {
        includes(NoteDiModule().module)
        includes(NoteUiDiModule().module)
    }
