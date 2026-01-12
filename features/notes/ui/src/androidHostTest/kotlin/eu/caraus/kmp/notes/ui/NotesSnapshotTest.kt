package eu.caraus.kmp.notes.ui
import kotlin.test.Test

class NotesSnapshotTest {

//    @get:Rule
//    val paparazzi = Paparazzi(
//        theme = "android:Theme.Material.Dark.NoActionBar",
//        deviceConfig = DeviceConfig.PIXEL_6_PRO
//    )

    @Test
    fun test() {
        val composePreviews = ComposePreviewsAggregator()
            .scan(this::class.java.packageName)
        composePreviews.forEach {
            println(it.name)
        }
    }
}





