package eu.caraus.kmp.notes.tests

import eu.caraus.kmp.test.common.previews.ComposePreviewsAggregator
import kotlin.test.Ignore
import kotlin.test.Test

class NotesSnapshotTest {
//    @get:Rule
//    val paparazzi = Paparazzi(
//        deviceConfig = PIXEL_5,
//        theme = "android:Theme.Material.Light.NoActionBar"
//        // ...see docs for more options
//    )

    @Test
    @Ignore("Wait for paparazzi 2.0.0-alpha05 to make this work")
    fun test() {
        val composePreviews =
            ComposePreviewsAggregator().scan(this::class.java.packageName)

//        composePreviews.forEach { previewFunction ->
//            CompositionLocalProvider(LocalInspectionMode provides true) {
//                PreviewContextConfigurationEffect()
//            }
//
//            SnapshotTestWrapper {
//                previewFunction.invoke(null, currentComposer)
//            }
//        }
    }
}
