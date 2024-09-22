package eu.caraus.kmp.samplearch.android

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.caraus.kmp.data.getDatabaseBuilder
import eu.caraus.kmp.notes.NoteFactory
import eu.caraus.kmp.samplearch.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDi(this)
        setContent { Content() }
    }
}

@Composable
fun Content() {
    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            App()
        }
    }
}

fun setupDi(applicationContext: Context) {
    NoteFactory.setupAppDatabase(getDatabaseBuilder(applicationContext).build())
}