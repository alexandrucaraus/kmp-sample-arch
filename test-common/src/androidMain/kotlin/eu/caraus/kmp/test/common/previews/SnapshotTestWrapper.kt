package eu.caraus.kmp.test.common.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SnapshotTestWrapper(
    modifier: Modifier = Modifier,
    toSnapshot: @Composable () -> Unit,
) {
    Box(
        modifier =
        modifier
            .background(Color.Black)
            .fillMaxSize(),
    ) {
        toSnapshot()
    }
}
