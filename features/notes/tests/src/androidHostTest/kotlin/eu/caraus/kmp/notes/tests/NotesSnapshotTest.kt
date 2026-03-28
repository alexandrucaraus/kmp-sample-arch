package eu.caraus.kmp.notes.tests

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.platform.LocalInspectionMode
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import eu.caraus.kmp.test.common.previews.ComposePreviewsAggregator
import eu.caraus.kmp.test.common.previews.SnapshotTestWrapper
import eu.caraus.kmp.test.common.previews.name
import org.junit.Rule
import kotlin.test.Test

class NotesSnapshotTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
    )

    @Test
    fun test() {
        val composePreviews =
            ComposePreviewsAggregator().scan("eu.caraus.kmp.notes")
        composePreviews.forEach { previewFunction ->
            paparazzi.snapshot(name = previewFunction.name()) {
                CompositionLocalProvider(LocalInspectionMode provides true) {
                    SnapshotTestWrapper {
                        val args = when (val paramCount = previewFunction.parameters.size) {
                            2 -> arrayOf(currentComposer, 0)
                            3 -> arrayOf(null, currentComposer, 0)
                            else -> Array(paramCount) { i ->
                                when (i) {
                                    paramCount - 2 -> currentComposer
                                    paramCount - 1 -> 0
                                    else -> null
                                }
                            }
                        }
                        previewFunction.invoke(null, *args)
                    }
                }
            }
        }
    }
}
