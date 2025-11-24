package eu.caraus.kmp.notes

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.ksp.generated.module



@Module
@ComponentScan("eu.caraus.kmp.notes")
class NoteDiModule

@Factory
fun createViewModelCoroutineScope(): CoroutineScope =
    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)