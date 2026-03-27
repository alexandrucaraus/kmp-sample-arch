package eu.caraus.kmp.notes.tests.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.caraus.kmp.notes.ui.list.NoteListScreen
import eu.caraus.kmp.notes.ui.list.NoteListState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displayNotesListScreen() {
        composeTestRule.setContent {
            NoteListScreen(state = NoteListState(), openNote = {}, createNote = {})
        }

        composeTestRule
            .onNodeWithTag("CreateNoteButton")
            .assertIsDisplayed()
            .performClick()
    }
}
