package eu.caraus.kmp.samplearch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module

@KoinApplication
object AppDi

@Module
@Configuration
class AppDiModule {
    @Factory
    fun createViewModelCoroutineScope() = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
}
