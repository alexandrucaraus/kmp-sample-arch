package eu.caraus.kmp.samplearch

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
import androidx.test.platform.app.InstrumentationRegistry
import eu.caraus.kmp.test.common.rules.KoinAndroidTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.plugin.module.dsl.startKoin
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class AppTest : KoinTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val koinAndroidTestRule =
        KoinAndroidTestRule {
            startKoin<AppDi> {
                androidContext(
                    InstrumentationRegistry.getInstrumentation().targetContext,
                )
            }
        }

    @Test
    fun sanityCheck() {
        composeTestRule.setContent {
            AppUi()
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
