package eu.caraus.kmp.notes.tests.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.caraus.kmp.database.room.AppDatabase
import eu.caraus.kmp.notes.tests.IntegrationTestDi
import eu.caraus.kmp.notes.ui.list.NoteListRoute
import eu.caraus.kmp.notes.ui.navigation.NotesNavGraph
import eu.caraus.kmp.notes.ui.navigation.NotesSerializerModule
import eu.caraus.kmp.test.common.navigation.NavDisplayTest
import eu.caraus.kmp.test.common.rules.KoinAndroidTestRule
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.plugin.module.dsl.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class NotesNavigationTest : KoinTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val koinAndroidTestRule =
        KoinAndroidTestRule {
            startKoin<IntegrationTestDi>()
        }

    @Before
    fun setup() {
        runBlocking {
            inject<AppDatabase>().value.clearAllTables()
        }
    }

    @Test
    fun list_notes_then_create_note_then_list_notes() {
        composeTestRule.setContent {
            NavDisplayTest(
                startDestination = NoteListRoute,
                serializerModule = NotesSerializerModule,
                guest = { key, backStack ->
                    NotesNavGraph(key, backStack) ?: error("Destination not found $key")
                },
            )
        }

        // Create Test note 1
        composeTestRule
            .onNodeWithTag("CreateNoteButton")
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithTag("TitleField")
            .assertIsDisplayed()
            .performTextInput("Test title 1")

        composeTestRule
            .onNodeWithTag("ContentField")
            .assertIsDisplayed()
            .performTextInput("Test content 1")

        composeTestRule
            .onNodeWithTag("BackButton")
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithText("Test title 1")
            .assertIsDisplayed()

        // Create test note 2
        composeTestRule
            .onNodeWithTag("CreateNoteButton")
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithTag("TitleField")
            .assertIsDisplayed()
            .performTextInput("Test title 2")

        composeTestRule
            .onNodeWithTag("ContentField")
            .assertIsDisplayed()
            .performTextInput("Test content 2")

        composeTestRule
            .onNodeWithTag("BackButton")
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithText("Test title 1")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Test title 2")
            .assertIsDisplayed()

        // Create test note 3
        composeTestRule
            .onNodeWithTag("CreateNoteButton")
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithTag("TitleField")
            .assertIsDisplayed()
            .performTextInput("Test title 3")

        composeTestRule
            .onNodeWithTag("ContentField")
            .assertIsDisplayed()
            .performTextInput("Test content 3")

        composeTestRule
            .onNodeWithTag("BackButton")
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithText("Test title 3")
            .assertIsDisplayed()

        // Check test note 1 in the list
        composeTestRule
            .onNodeWithText("Test title 1")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Test content 1")
            .assertIsDisplayed()

        // Check test note 2 in the list
        composeTestRule
            .onNodeWithText("Test title 2")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Test content 2")
            .assertIsDisplayed()

        // Update test note 1
        composeTestRule
            .onNodeWithText("Test title 1")
            .performClick()

        composeTestRule
            .onNodeWithTag("TitleField")
            .assertIsDisplayed()
            .performClick()
            .performTextClearance()

        composeTestRule
            .onNodeWithTag("TitleField")
            .performTextInput("Test title 1 updated")

        composeTestRule
            .onNodeWithTag("ContentField")
            .assertIsDisplayed()
            .performClick()
            .performTextClearance()

        composeTestRule
            .onNodeWithTag("ContentField")
            .performTextInput("Test content 1 updated")

        composeTestRule
            .onNodeWithTag("BackButton")
            .assertIsDisplayed()
            .performClick()

        // Check updates on note 1 in the list
        composeTestRule
            .onNodeWithText("Test title 1 updated")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Test content 1 updated")
            .assertIsDisplayed()

        // Check note 2 in the list
        composeTestRule
            .onNodeWithText("Test title 2")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Test content 2")
            .assertIsDisplayed()

        // Select both notes
        composeTestRule
            .onNodeWithText("Test title 1 updated")
            .assertIsDisplayed()
            .performTouchInput { longClick() }

        composeTestRule
            .onNodeWithText("Test title 2")
            .assertIsDisplayed()
            .performTouchInput { longClick() }

        // Check selection and delete buttons appear
        composeTestRule
            .onNodeWithTag("ClearSelectionsButton")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("DeleteSelectionsButton")
            .assertIsDisplayed()
            .performClick()

        // Check the list is empty after deletion
        composeTestRule
            .onNodeWithText("Test title 1 updated")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText("Test title 2")
            .assertDoesNotExist()
    }
}
