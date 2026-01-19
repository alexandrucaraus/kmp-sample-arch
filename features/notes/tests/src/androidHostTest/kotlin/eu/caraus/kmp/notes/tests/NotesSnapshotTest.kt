package eu.caraus.kmp.notes.tests

import eu.caraus.kmp.test.common.previews.ComposePreviewsAggregator
import kotlin.test.Test

class NotesSnapshotTest {
    @Test
    fun test() {
        val composePreviews =
            ComposePreviewsAggregator()
                .scan(this::class.java.packageName)
        composePreviews.forEach {
            println(it.name)
        }
    }
}
