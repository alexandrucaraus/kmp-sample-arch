package eu.caraus.kmp.samplearch

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.koin.dsl.KoinAppDeclaration

fun koinAppDeclaration(applicationContext: Context): KoinAppDeclaration {
    return {
        androidContext(applicationContext)
        androidLogger(Level.ERROR)
        modules(*appDiModules.toTypedArray())
    }
}
