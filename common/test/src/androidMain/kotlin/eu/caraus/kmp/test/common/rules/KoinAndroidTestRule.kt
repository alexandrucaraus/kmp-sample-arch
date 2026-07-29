package eu.caraus.kmp.test.common.rules

import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.core.KoinApplication
import org.koin.core.context.stopKoin
import org.koin.core.error.KoinApplicationAlreadyStartedException

class KoinAndroidTestRule(
    private val start: (appDeclaration: KoinApplication.() -> Unit) -> Unit,
) : TestWatcher() {
    override fun starting(description: Description) {
        try {
            start {}
        } catch (_: KoinApplicationAlreadyStartedException) {
            stopKoin()
            start {}
        }
    }

    override fun finished(description: Description) {
        stopKoin()
    }
}
