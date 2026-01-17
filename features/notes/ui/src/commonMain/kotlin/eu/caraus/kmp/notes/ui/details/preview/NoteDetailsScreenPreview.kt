package eu.caraus.kmp.notes.ui.details.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import eu.caraus.kmp.notes.ui.details.NoteDetailsScreen
import eu.caraus.kmp.notes.ui.details.NoteState

@Composable
@Preview
fun NoteDetailsScreenPreview() {
    NoteDetailsScreen(
        note =
            NoteState(
                title = "Preview title",
                content = "Preview content",
            ),
        close = {},
    )
}
