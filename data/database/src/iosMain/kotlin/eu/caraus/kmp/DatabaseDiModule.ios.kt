package eu.caraus.kmp

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.module.Module as DiModule
import org.koin.dsl.module
import org.koin.ksp.generated.module

@Module
@ComponentScan
class PlatformDatabaseDiModule

actual fun platformDatabaseDiModule(): List<DiModule> = listOf(
    PlatformDatabaseDiModule().module
)