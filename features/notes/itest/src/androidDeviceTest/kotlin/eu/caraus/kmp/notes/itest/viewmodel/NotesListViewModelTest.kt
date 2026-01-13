@file:OptIn(FlowPreview::class)

package eu.caraus.kmp.notes.itest.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.SaveNoteUseCase
import eu.caraus.kmp.notes.itest.NoteIntegrationTestModule
import eu.caraus.kmp.notes.ui.list.NoteListViewModel
import eu.caraus.kmp.test.common.rules.KoinTestRule
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

@RunWith(AndroidJUnit4::class)
class NotesListViewModelTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule(modules = listOf(NoteIntegrationTestModule().module))

    @Test
    fun load_select_delete_notes() = runBlocking {

        val saveNoteUseCase by inject<SaveNoteUseCase>()
        val viewModel by inject<NoteListViewModel>()

        // add a few notes
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

        saveNoteUseCase(
            noteId = Note.NO_ID,
            title = "test3",
            content = "test3"
        )

        // wait to load
        viewModel.state.takeWhile { it.notes.isEmpty() }.timeout(3.seconds).collect()

        val loadedNotes = viewModel.state.value.notes

        assertTrue("Notes not loaded") {
            loadedNotes.size == 3
        }

        // select
        viewModel.state.value.toggleSelection(loadedNotes[0])

        viewModel.state.value.toggleSelection(loadedNotes[1])

        assertTrue("Notes not selected") {
            viewModel.state.value.selectedNotes.size == 2
        }

        // delete
        viewModel.state.value.deleteSelected()

        viewModel.state.takeWhile { it.notes.size != 1 }.timeout(3.seconds).collect()

        assertTrue("Selected notes not cleared") {
            viewModel.state.value.selectedNotes.isEmpty()
        }
        assertTrue("Notes not deleted") {
            viewModel.state.value.notes.size == 1
        }
    }
}