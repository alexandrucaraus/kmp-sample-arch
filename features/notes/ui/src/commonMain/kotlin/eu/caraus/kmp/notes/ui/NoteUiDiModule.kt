package eu.caraus.kmp.notes.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module



@Module
@ComponentScan("eu.caraus.kmp.notes.ui")
@Configuration
class NoteUiDiModule

@Factory
fun createViewModelCoroutineScope(): CoroutineScope =
    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
