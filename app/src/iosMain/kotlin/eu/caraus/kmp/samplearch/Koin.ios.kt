package eu.caraus.kmp.samplearch

import org.koin.core.context.startKoin

fun startKoin() = startKoin {
    modules(*appDiModules.toTypedArray())
}