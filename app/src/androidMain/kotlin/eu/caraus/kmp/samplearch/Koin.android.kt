package eu.caraus.kmp.samplearch

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.koin.plugin.module.dsl.startKoin

//import org.koin.android.ext.koin.androidContext
//import org.koin.android.ext.koin.androidLogger
//import org.koin.core.logger.Level
//import org.koin.generated.startKoin

fun startKoin(applicationContext: Context) =
    startKoin<AppDi> {
      androidContext(applicationContext)
      androidLogger(Level.ERROR)

    }
//    AppDi.startKoin {
//        androidContext(applicationContext)
//        androidLogger(Level.ERROR)
//    }
