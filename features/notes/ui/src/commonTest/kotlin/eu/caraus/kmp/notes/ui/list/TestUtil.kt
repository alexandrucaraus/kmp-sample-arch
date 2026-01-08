package eu.caraus.kmp.notes.ui.list

import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.koin.test.KoinTest
import kotlin.test.Test

fun KoinTest.startTestKoin() = org.koin.core.context.startKoin {
    modules(noteUiTestKoinModule())
}

fun KoinTest.stopTestKoin() = org.koin.core.context.stopKoin()

fun test(
    before: () -> Unit = {},
    after: () -> Unit = {},
    block: suspend TestScope.() -> Unit,
) = runTest {
    try {
        before()
        block()
    } catch (e: Throwable) {

    } finally {
        after()
    }
}
