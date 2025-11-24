package eu.caraus.kmp.samplearch.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import eu.caraus.kmp.samplearch.AndroidApp

class Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidApp()
    }
}
