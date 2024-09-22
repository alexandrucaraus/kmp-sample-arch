package eu.caraus.kmp.notes.presentation.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class NoteState(
    val title: String = "",
    val content: String = "",
    val updateTitle: (String) -> Unit = {},
    val updateContent: (String) -> Unit = {},
    val leave: (() -> Unit) -> Unit = {},
    val delete: (() -> Unit) -> Unit = {},
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailsScreen(
    note: NoteState,
    close: () -> Unit,
) = Scaffold(
    modifier = Modifier,
    topBar = {
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = { note.leave(close) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(onClick = { note.delete(close) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
            }
        )
    },
    content = { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxWidth()) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = note.title,
                onValueChange = note.updateTitle,
                textStyle = TextStyle(fontSize = 20.sp),
                placeholder = { Text("Title") }
            )
            TextField(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                value = note.content,
                onValueChange = note.updateContent,
                placeholder = { Text("Note") }
            )
        }
    },
)
