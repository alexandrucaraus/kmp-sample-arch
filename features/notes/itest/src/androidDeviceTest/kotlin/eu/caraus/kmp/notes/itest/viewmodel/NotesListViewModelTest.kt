@file:OptIn(ExperimentalCoroutinesApi::class)

package eu.caraus.kmp.notes.itest.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.SaveNoteUseCase
import eu.caraus.kmp.notes.itest.NoteIntegrationTestModule
import eu.caraus.kmp.notes.ui.list.NoteListViewModel
import eu.caraus.kmp.test.common.rules.KoinTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class NotesListViewModelTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule(modules = listOf(NoteIntegrationTestModule().module))

    @Test
    fun list_notes() = runBlocking {

        val saveNoteUseCase by inject<SaveNoteUseCase>()

        saveNoteUseCase(
            noteId = Note.NO_ID,
            title = "test1",
            content = "test1"
        )

        saveNoteUseCase(
            noteId = Note.NO_ID,
            title = "test2",
            content = "test2"
        )

        // todo the bellow is not good cause it is using delays very high flakiness potential
        val vm by inject<NoteListViewModel>()

        delay(1000)

        val state1 = vm.state.first()

        delay(1000)

        println(state1.notes)

        val state2 = vm.state.first()

        delay(1000)

        println(state2.notes)

        val state3 = vm.state.first()

        println(state3.notes)

        assertTrue { state3.notes.size == 2 }

    }

    @Test
    fun select_notes() {

    }

    @Test
    fun delete_notes() {

    }
}