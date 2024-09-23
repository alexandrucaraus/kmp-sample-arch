package eu.caraus.kmp.samplearch.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import eu.caraus.kmp.samplearch.App
import eu.caraus.kmp.samplearch.setupDi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDi(this)
        setContent { App() }
    }
}

