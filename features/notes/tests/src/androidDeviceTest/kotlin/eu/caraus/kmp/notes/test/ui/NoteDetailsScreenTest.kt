package eu.caraus.kmp.notes.test.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.caraus.kmp.notes.ui.details.NoteDetailsScreen
import eu.caraus.kmp.notes.ui.details.NoteState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteDetailsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displayNotesListScreen() {
        composeTestRule.setContent {
            NoteDetailsScreen(
                note = NoteState(),
                close = {},
            )
        }

        composeTestRule
            .onNodeWithTag("TitleField")
            .assertIsDisplayed()
            .performTextInput("Test title")

        composeTestRule
            .onNodeWithTag("ContentField")
            .assertIsDisplayed()
            .performTextInput("Content title")

        composeTestRule
            .onNodeWithTag("BackButton")
            .assertIsDisplayed()
            .performClick()
    }
}
