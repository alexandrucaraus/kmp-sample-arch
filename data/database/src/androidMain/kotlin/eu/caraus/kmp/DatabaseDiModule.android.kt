package eu.caraus.kmp

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.ksp.generated.module
import org.koin.core.module.Module as DiModule

@Module
@ComponentScan
class PlatformDatabaseDiModule

actual fun platformDatabaseDiModule(): List<DiModule> = listOf(
    PlatformDatabaseDiModule().module
)