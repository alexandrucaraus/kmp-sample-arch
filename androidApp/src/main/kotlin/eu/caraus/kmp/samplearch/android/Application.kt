package eu.caraus.kmp.samplearch.android

import android.app.Application
import eu.caraus.kmp.samplearch.startKoin

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(applicationContext)
    }
}