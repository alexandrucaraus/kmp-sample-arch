@file:OptIn(ExperimentalCoroutinesApi::class)

package eu.caraus.kmp.notes.ui.list

import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import org.koin.test.KoinTest
import kotlin.test.Test
import kotlin.test.assertTrue
import org.koin.test.inject
import org.koin.test.mock.declare

class NoteListViewModelTest : KoinTest {

    @Test
    fun list_notes_on_init() = test {
        declare<CoroutineScope> { this }
        declare<NoteRepository>{
            object : NoteRepositoryMock() {
                override fun allAsFlow(): Flow<List<Note>> {
                    return flowOf(
                        listOf(
                            Note(id = "1", "test", "test")
                        )
                    )
                }
            }
        }
        val vm by inject<NoteListViewModel>()
        vm.state.first()
        runCurrent()
        assertTrue(vm.state.value.notes.isNotEmpty())
    }

    @Test
    fun select_multiple_notes() = test {
        declare<CoroutineScope> { this }
        declare<NoteRepository>{
            object : NoteRepositoryMock() {
                override fun allAsFlow(): Flow<List<Note>> {
                    return flowOf(
                        listOf(
                            Note(id = "1", "test1", "test1"),
                            Note(id = "2", "test2", "test2"),
                            Note(id = "3", "test3", "test3"),
                            Note(id = "4", "test4", "test4")
                        )
                    )
                }
            }
        }
        val vm by inject<NoteListViewModel>()
        vm.state.first()
        runCurrent()

        val notes = vm.state.value.notes

        vm.state.value.toggleSelection(notes[0])

        runCurrent()

        assertTrue(vm.state.value.selectedNotes.contains(notes[0]))

        vm.state.value.toggleSelection(notes[1])

        runCurrent()

        assertTrue { vm.state.value.selectedNotes.contains(notes[1]) }

        vm.state.value.clearSelected()

        runCurrent()

        assertTrue { vm.state.value.selectedNotes.isEmpty() }
    }

    @Test
    fun delete_selected_notes() = test {
        startTestKoin()
        declare<CoroutineScope> { this }
        declare<NoteRepository>{
            object : NoteRepositoryMock() {
                override fun allAsFlow(): Flow<List<Note>> {
                    return flowOf(
                        listOf(
                            Note(id = "1", "test1", "test1"),
                            Note(id = "2", "test2", "test2"),
                            Note(id = "3", "test3", "test3"),
                            Note(id = "4", "test4", "test4")
                        )
                    )
                }
            }
        }
        val vm by inject<NoteListViewModel>()
        vm.state.first()

        runCurrent()

        assertTrue { vm.state.value.notes.size == 4 }

        val notes = vm.state.value.notes

        vm.state.value.toggleSelection(notes[0])

        runCurrent()

        assertTrue(vm.state.value.selectedNotes.contains(notes[0]))

        vm.state.value.toggleSelection(notes[1])

        runCurrent()

        assertTrue { vm.state.value.selectedNotes.contains(notes[1]) }

        vm.state.value.deleteSelected()

        runCurrent()

        assertTrue { vm.state.value.selectedNotes.isEmpty() }
        assertTrue { vm.state.value.notes.size == 2 }
    }

    fun test(block: suspend TestScope.() -> Unit) =
        test(before = { startTestKoin() }, after = { stopTestKoin()}, block = block)
}
