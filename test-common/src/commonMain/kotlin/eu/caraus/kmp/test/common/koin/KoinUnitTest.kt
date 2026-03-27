package eu.caraus.kmp.test.common.koin

import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.koin.core.context.stopKoin

/**
 * Generic koin unit test runner
 * with setup before and teardown after
 */
fun koinRunTest(
    setupBefore: () -> Unit = {},
    cleanUpAfter: () -> Unit = { stopKoin() },
    testBlock: suspend TestScope.() -> Unit,
) = runTest {
    try {
        setupBefore()
        testBlock()
    } finally {
        cleanUpAfter()
    }
}
