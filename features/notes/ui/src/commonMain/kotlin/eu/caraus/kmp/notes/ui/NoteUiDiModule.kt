package eu.caraus.kmp.notes.ui

import eu.caraus.kmp.notes.domain.NoteDiModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module

@Module(
    includes = [
        NoteDiModule::class
    ]
)
@ComponentScan("eu.caraus.kmp.notes.ui")
@Configuration
class NoteUiDiModule
