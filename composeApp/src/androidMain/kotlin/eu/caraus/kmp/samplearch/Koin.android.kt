package eu.caraus.kmp.samplearch

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

fun startKoin(applicationContext: Context) = startKoin {
    androidContext(applicationContext)
    androidLogger(Level.ERROR)
    modules(*appDiModules.toTypedArray())
}
