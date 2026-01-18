@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package eu.caraus.kmp.notes.ui.list

import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.NoteRepository
import eu.caraus.kmp.notes.domain.NoteRepositoryInMem
import eu.caraus.kmp.notes.test.commonTestModules
import eu.caraus.kmp.test.common.koin.koinRunTest
import eu.caraus.kmp.test.common.koin.startTestKoin
import eu.caraus.kmp.test.common.koin.stopTestKoin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declare
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class NoteListViewModelTest : KoinTest {
    @Test
    fun list_notes_on_init() =
        test {
            declare<CoroutineScope> { backgroundScope }
            declare<NoteRepository> {
                NoteRepositoryInMem(
                    listOf(
                        Note(id = "1", "test", "test"),
                    ),
                )
            }
            val vm by inject<NoteListViewModel>()
            vm.state.first()
            runCurrent()
            assertTrue(
                vm.state.value.notes
                    .isNotEmpty(),
            )
        }

    @Test
    fun select_multiple_notes() =
        test {
            declare<CoroutineScope> { backgroundScope }
            declare<NoteRepository> {
                NoteRepositoryInMem(
                    listOf(
                        Note(id = "1", "test1", "test1"),
                        Note(id = "2", "test2", "test2"),
                        Note(id = "3", "test3", "test3"),
                        Note(id = "4", "test4", "test4"),
                    ),
                )
            }

            val vm by inject<NoteListViewModel>()

            vm.state
                .takeWhile { it.notes.isEmpty() }
                .timeout(3.seconds)
                .collect()

            runCurrent()

            val notes = vm.state.value.notes

            vm.state.value.toggleSelection(notes[0])

            runCurrent()

            assertTrue(
                vm.state.value.selectedNotes
                    .contains(notes[0]),
            )

            vm.state.value.toggleSelection(notes[1])

            runCurrent()

            assertTrue {
                vm.state.value.selectedNotes
                    .contains(notes[1])
            }

            vm.state.value.clearSelected()

            runCurrent()

            assertTrue("Selected notes not empty") {
                vm.state.value.selectedNotes
                    .isEmpty()
            }
        }

    @Test
    fun delete_selected_notes() =
        test {
            declare<CoroutineScope> { backgroundScope }
            declare<NoteRepository> {
                NoteRepositoryInMem(
                    listOf(
                        Note(id = "1", "test1", "test1"),
                        Note(id = "2", "test2", "test2"),
                        Note(id = "3", "test3", "test3"),
                        Note(id = "4", "test4", "test4"),
                    ),
                )
            }
            val vm by inject<NoteListViewModel>()

            vm.state
                .takeWhile { it.notes.isEmpty() }
                .timeout(3.seconds)
                .collect()

            runCurrent()

            assertTrue("Failed to load") { vm.state.value.notes.size == 4 }

            val notes = vm.state.value.notes

            vm.state.value.toggleSelection(notes[0])

            runCurrent()

            assertTrue("No note 1") {
                vm.state.value.selectedNotes
                    .contains(notes[0])
            }

            vm.state.value.toggleSelection(notes[1])

            runCurrent()

            assertTrue("No note 2") {
                vm.state.value.selectedNotes
                    .contains(notes[1])
            }

            vm.state.value.deleteSelected()

            runCurrent()

            assertTrue("Selected notes not empty") {
                vm.state.value.selectedNotes
                    .isEmpty()
            }
            assertTrue("Note size does not match") { vm.state.value.notes.size == 2 }
        }

    private fun test(block: suspend TestScope.() -> Unit) =
        koinRunTest(
            before = { startTestKoin(modules = listOf(commonTestModules())) },
            after = { stopTestKoin() },
            block = block,
        )
}
