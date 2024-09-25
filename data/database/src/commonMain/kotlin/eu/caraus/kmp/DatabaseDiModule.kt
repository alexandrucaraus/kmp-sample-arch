package eu.caraus.kmp


import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.ksp.generated.module
import org.koin.core.module.Module as DiModule

@Module
@ComponentScan
class DatabaseDiModule

expect fun platformDatabaseDiModule(): List<DiModule>

val databaseDiModules: List<DiModule> = listOf(DatabaseDiModule().module) + platformDatabaseDiModule()