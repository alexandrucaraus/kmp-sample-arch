@file:Suppress("FunctionName")

package eu.caraus.kmp.samplearch

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

fun ComponentActivity.App(applicationContext: Context) = setContent {
    App(koinAppDeclaration(applicationContext))
}