package eu.caraus.kmp.samplearch

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.koin.ksp.generated.startKoin

fun startKoin(applicationContext: Context) = AppDi.startKoin {
    androidContext(applicationContext)
    androidLogger(Level.ERROR)
}
