package eu.caraus.kmp.samplearch

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.caraus.kmp.test.common.rules.KoinTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.ksp.generated.configurationModules
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class AppTest : KoinTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val koinTestRule = KoinTestRule(modules = AppDi.configurationModules)

    @Test
    fun sanityCheck() {
        composeTestRule.setContent {
            AppUi()
        }

        composeTestRule
            .onNodeWithTag("CreateNoteButton")
            .assertIsDisplayed()
    }
}
