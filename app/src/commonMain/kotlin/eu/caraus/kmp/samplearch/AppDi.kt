package eu.caraus.kmp.samplearch

import eu.caraus.kmp.database.DatabaseDiModule
import eu.caraus.kmp.notes.NoteDiModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@ComponentScan("eu.caraus")
@Module(
    includes = [
        DatabaseDiModule::class,
        NoteDiModule::class,
    ]
)
class AppDi
