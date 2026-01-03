package eu.caraus.kmp.samplearch

import org.koin.core.context.startKoin
import org.koin.ksp.generated.*

fun startKoin() = startKoin {
    // TODO test
    modules(AppDi.module)
}