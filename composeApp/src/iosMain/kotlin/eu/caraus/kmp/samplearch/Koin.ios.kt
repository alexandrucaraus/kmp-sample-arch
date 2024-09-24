package eu.caraus.kmp.samplearch

import org.koin.dsl.KoinAppDeclaration

fun koinAppDeclaration(): KoinAppDeclaration {
    return {
        modules(*appDiModules.toTypedArray())
    }
}