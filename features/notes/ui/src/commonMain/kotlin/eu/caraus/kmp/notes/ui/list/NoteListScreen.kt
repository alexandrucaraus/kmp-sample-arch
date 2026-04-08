package eu.caraus.kmp.notes.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.usecases.testFunction

private val thinBorder = 0.5.dp
private val thickBorder = 2.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    state: NoteListState,
    modifier: Modifier = Modifier,
    openNote: (Note) -> Unit = {},
    createNote: () -> Unit = {},
) = Scaffold(
    modifier = modifier,
    topBar = {
        TopAppBar(
            title = { Text("Notes") },
            actions = { DeleteSelectionsButton(state) },
            navigationIcon = { ClearSelectionsButton(state) },
        )
    },
    content = { padding ->
        testFunction("testing")
        if (state.notes.isEmpty()) {
            EmptyNoteList()
        } else {
            NoteList(
                modifier = Modifier.padding(padding),
                state = state,
                openNote = openNote,
            )
        }
    },
    floatingActionButton = { CreateNoteButton(createNote) },
)

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val selectedNotes: List<Note> = emptyList(),
    val toggleSelection: (Note) -> Unit = {},
    val deleteSelected: () -> Unit = {},
    val clearSelected: () -> Unit = {},
)

@Composable
internal fun NoteList(
    state: NoteListState,
    openNote: (Note) -> Unit,
    modifier: Modifier = Modifier,
) = LazyColumn(
    modifier = modifier.padding(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
) {
    items(state.notes) { note ->
        NoteListItem(
            note = note,
            openNote = openNote,
            isSelected = note in state.selectedNotes,
            selectNote = state.toggleSelection,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NoteListItem(
    note: Note,
    modifier: Modifier = Modifier,
    openNote: (Note) -> Unit = {},
    selectNote: (Note) -> Unit = {},
    isSelected: Boolean = false,
) {
    val borderWidth = if (isSelected) thickBorder else thinBorder
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else DarkGray
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .border(borderWidth, borderColor, shape = MaterialTheme.shapes.medium)
                .combinedClickable(
                    enabled = true,
                    onClick = {
                        openNote(note)
                    },
                    onLongClick = {
                        selectNote(note)
                    },
                ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
        ) {
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = note.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = note.content,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
internal fun EmptyNoteList() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(modifier = Modifier.align(Alignment.Center), text = "No notes yet")
    }
}

@Composable
internal fun CreateNoteButton(createNote: () -> Unit) {
    Button(
        modifier = Modifier.testTag("CreateNoteButton"),
        onClick = createNote,
    ) {
        Icon(
            imageVector = Icons.Default.PlusOne,
            contentDescription = null,
        )
    }
}

@Composable
internal fun DeleteSelectionsButton(state: NoteListState) {
    if (state.selectedNotes.isNotEmpty()) {
        IconButton(
            modifier = Modifier.testTag("DeleteSelectionsButton"),
            onClick = state.deleteSelected,
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
            )
        }
    }
}

@Composable
internal fun ClearSelectionsButton(state: NoteListState) {
    if (state.selectedNotes.isNotEmpty()) {
        IconButton(
            modifier = Modifier.testTag("ClearSelectionsButton"),
            onClick = state.clearSelected,
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
            )
        }
    }
}
