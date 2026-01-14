package eu.caraus.kmp.test.common.koin

import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest

fun KoinTest.startTestKoin(
    modules: List<Module> = emptyList()
) = startKoin {
    modules(modules)
}

fun KoinTest.stopTestKoin() = stopKoin()

fun koinRunTest(
    before: () -> Unit = {},
    after: () -> Unit = {},
    block: suspend TestScope.() -> Unit,
) = runTest {
    try {
        before()
        block()
    } catch (e: Throwable) {
        throw e
    } finally {
        after()
    }
}
