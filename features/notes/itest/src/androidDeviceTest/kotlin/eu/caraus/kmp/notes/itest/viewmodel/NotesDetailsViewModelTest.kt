@file:OptIn(FlowPreview::class)

package eu.caraus.kmp.notes.itest.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.caraus.kmp.notes.domain.GetNoteUseCase
import eu.caraus.kmp.notes.domain.GetNotesListUseCase
import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.SaveNoteUseCase
import eu.caraus.kmp.notes.itest.NoteIntegrationTestModule
import eu.caraus.kmp.notes.ui.details.NoteDetailsViewModel
import eu.caraus.kmp.test.common.rules.KoinTestRule
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.parameter.parametersOf
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds


@RunWith(AndroidJUnit4::class)
class NotesDetailsViewModelTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule(modules = listOf(NoteIntegrationTestModule().module))

    @Test
    fun load_edit_and_save_note() = runBlocking {

        val saveNoteUseCase by inject<SaveNoteUseCase>()
        val getNotesListUseCase by inject<GetNotesListUseCase>()
        val getNoteUseCase by inject<GetNoteUseCase>()

        saveNoteUseCase(
            noteId = Note.NO_ID,
            title = "test1",
            content = "test1"
        )

        getNotesListUseCase().takeWhile { it.isEmpty() }.timeout(3.seconds).collect()

        val note = getNotesListUseCase().first().first()
        val noteId = note.id

        assertTrue("Note not listed") {
            note.title == "test1"
        }

        val viewModel by inject<NoteDetailsViewModel> {
            parametersOf(note.id)
        }

        // wait to load
        viewModel.state.takeWhile { it.title != "test1" }.timeout(3.seconds).collect()

        assertTrue("Note not loaded") {
            viewModel.state.value.title == "test1"
        }

        // edit

        viewModel.state.value.updateTitle("test1TitleUpdated")
        viewModel.state.value.updateContent("test1ContentUpdated")

        assertTrue("Title not updated in viewModel") {
            viewModel.state.value.title == "test1TitleUpdated"
        }
        assertTrue("Content not updated in viewModel") {
            viewModel.state.value.content == "test1ContentUpdated"
        }

        // close and save
        viewModel.state.value.leave { }

        // wait for changes to reflect in the db
        getNotesListUseCase()
            .takeWhile { notes -> notes.any { it.title != "test1TitleUpdated" } }
            .timeout(3.seconds)
            .collect()

        val loadedNote = getNoteUseCase(noteId).first()

        assertTrue("Title not updated in db") {
            loadedNote.title == "test1TitleUpdated"
        }
        assertTrue("Content not updated in db") {
            loadedNote.content == "test1ContentUpdated"
        }
    }
}
