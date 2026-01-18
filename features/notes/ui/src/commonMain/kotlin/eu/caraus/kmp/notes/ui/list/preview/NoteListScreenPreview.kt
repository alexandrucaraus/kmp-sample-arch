package eu.caraus.kmp.notes.ui.list.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import eu.caraus.kmp.notes.ui.list.NoteListScreen
import eu.caraus.kmp.notes.ui.list.NoteListState

@Composable
@Preview
internal fun NoteListScreenPreview() {
    NoteListScreen(
        state = NoteListState(),
        openNote = {},
        createNote = {},
    )
}
