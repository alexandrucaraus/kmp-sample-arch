@file:Suppress("FunctionName")

package eu.caraus.kmp.samplearch

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun JvmApp() {
    startKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "KMP Sample Arch",
        ) {
            AppUi()
        }
    }
}
