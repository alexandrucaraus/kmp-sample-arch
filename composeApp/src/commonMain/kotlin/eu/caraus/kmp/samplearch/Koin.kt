package eu.caraus.kmp.samplearch

import eu.caraus.kmp.database.DatabaseDiModule
import eu.caraus.kmp.notes.NoteDiModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.ksp.generated.module


//val appDiModules = databaseDiModules + notesDiModules

@Module
@ComponentScan("eu.caraus")
class AppModule {
    val modules = listOf(
        NoteDiModule().module,
        DatabaseDiModule().module,
    )
}
