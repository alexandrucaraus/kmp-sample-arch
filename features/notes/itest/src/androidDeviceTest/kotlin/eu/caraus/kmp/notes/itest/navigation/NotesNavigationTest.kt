package eu.caraus.kmp.notes.itest.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.caraus.kmp.notes.itest.NoteIntegrationTestModule
import eu.caraus.kmp.notes.ui.list.NoteListRoute
import eu.caraus.kmp.notes.ui.navigation.notesNavGraph
import eu.caraus.kmp.test.common.navigation.NavHostTest
import eu.caraus.kmp.test.common.rules.KoinTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.ksp.generated.module
import org.koin.test.KoinTest


@RunWith(AndroidJUnit4::class)
class NotesNavigationTest : KoinTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val koinTestRule = KoinTestRule(modules = listOf(NoteIntegrationTestModule().module))

    @Test
    fun notesNavigation() {
        composeTestRule.setContent {
            NavHostTest(
                startDestination = NoteListRoute,
            ) { navController ->
                notesNavGraph(navController)
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
    }
}
