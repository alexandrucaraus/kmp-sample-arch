package eu.caraus.kmp.notes.itest.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.caraus.kmp.database.room.AppDatabase
import eu.caraus.kmp.notes.itest.NoteIntegrationTestModule
import eu.caraus.kmp.notes.ui.list.NoteListRoute
import eu.caraus.kmp.notes.ui.navigation.NotesNavGraph
import eu.caraus.kmp.notes.ui.navigation.NotesSerializerModule
import eu.caraus.kmp.test.common.navigation.NavHostTest
import eu.caraus.kmp.test.common.rules.KoinTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class NotesNavigationTest : KoinTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val koinTestRule = KoinTestRule(modules = listOf(NoteIntegrationTestModule().module))

    @Before
    fun setup() {
        inject<AppDatabase>().value.clearAllTables()
    }

    @Test
    fun list_notes_then_create_note_then_list_notes() {
        composeTestRule.setContent {
            NavHostTest(
                startDestination = NoteListRoute,
                serializerModule = NotesSerializerModule,
            ) { key, backStack ->
                NotesNavGraph(key, backStack)
                    ?: error("Destination not found $key")
            }
        }
        composeTestRule
            .onNodeWithTag("CreateNoteButton")
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithTag("TitleField")
            .assertIsDisplayed()
            .performTextInput("Test title")

        composeTestRule
            .onNodeWithTag("ContentField")
            .assertIsDisplayed()
            .performTextInput("Test content")

        composeTestRule
            .onNodeWithTag("BackButton")
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithText("Test title")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Test content")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Test title")
            .performTouchInput { longClick() }

        composeTestRule
            .onNodeWithTag("ClearSelectionsButton")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("DeleteSelectionsButton")
            .assertIsDisplayed()
            .performClick()
    }
}
