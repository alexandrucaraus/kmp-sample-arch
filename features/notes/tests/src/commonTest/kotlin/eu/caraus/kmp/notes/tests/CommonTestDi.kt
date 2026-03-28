package eu.caraus.kmp.notes.tests

import eu.caraus.kmp.notes.domain.NoteRepository
import eu.caraus.kmp.notes.domain.NoteRepositoryInMem
import eu.caraus.kmp.test.common.koin.koinRunTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.TestScope
import org.koin.core.annotation.Factory
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.plugin.module.dsl.startKoin
import eu.caraus.kmp.notes.domain.Di as NoteDomainDi
import eu.caraus.kmp.notes.ui.Di as NoteUiDi

@Module
object CommonTestSubstitutes {
    @Factory
    fun createViewModelCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    @Factory
    fun repo(): NoteRepository = NoteRepositoryInMem()
}

@KoinApplication(
    configurations = [""],
    modules = [
        NoteDomainDi::class,
        NoteUiDi::class,
        CommonTestSubstitutes::class,
    ],
)
object CommonTestDi

/**
 * Runs a koin test with dependencies setup
 * for this specific module
 */
fun koinTest(block: suspend TestScope.() -> Unit) =
    koinRunTest(
        setupBefore = { startKoin<CommonTestDi>() },
        testBlock = block,
    )
