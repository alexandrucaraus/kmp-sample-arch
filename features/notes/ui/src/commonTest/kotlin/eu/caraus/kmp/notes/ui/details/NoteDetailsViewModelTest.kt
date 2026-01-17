@file:OptIn(ExperimentalCoroutinesApi::class)

package eu.caraus.kmp.notes.ui.details

import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.NoteRepository
import eu.caraus.kmp.notes.domain.NoteRepositoryInMem
import eu.caraus.kmp.notes.ui.noteUiTestKoinModule
import eu.caraus.kmp.test.common.koin.koinRunTest
import eu.caraus.kmp.test.common.koin.startTestKoin
import eu.caraus.kmp.test.common.koin.stopTestKoin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declare
import kotlin.test.DefaultAsserter.assertEquals
import kotlin.test.Test
import kotlin.test.assertEquals

class NoteDetailsViewModelTest : KoinTest {
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
            val vm by inject<NoteDetailsViewModel> {
                parametersOf("1")
            }
            vm.state.first()
            runCurrent()
            assertEquals(vm.state.value.title, "test")
            assertEquals(vm.state.value.content, "test")
        }

    @Test
    fun edit_note() =
        test {
            declare<CoroutineScope> { backgroundScope }
            declare<NoteRepository> {
                NoteRepositoryInMem(
                    listOf(
                        Note(id = "3", "test3", "test3"),
                    ),
                )
            }

            val vm by inject<NoteDetailsViewModel> {
                parametersOf("3")
            }

            val state = vm.state.first()

            runCurrent()

            state.updateTitle("updateNote3Title")

            runCurrent()

            state.updateContent("updatedNote3Content")

            runCurrent()

            assertEquals(
                "Title did not change",
                "updateNote3Title",
                vm.state.value.title,
            )
            assertEquals(
                "Content did not change",
                "updatedNote3Content",
                vm.state.value.content,
            )
        }

    @Test
    fun delete_note() =
        test {
            declare<CoroutineScope> { backgroundScope }
            declare<NoteRepository> {
                NoteRepositoryInMem(
                    listOf(
                        Note(id = "4", "test4", "test4"),
                    ),
                )
            }
            val vm by inject<NoteDetailsViewModel> {
                parametersOf("4")
            }

            val state = vm.state.first()

            runCurrent()

            state.updateTitle("updateNote3Title")
            state.updateContent("updatedNote3Content")

            runCurrent()

            assertEquals(
                "Title did not change",
                "updateNote3Title",
                vm.state.value.title,
            )
            assertEquals(
                "Content did not change",
                "updatedNote3Content",
                vm.state.value.content,
            )

            var action = "to_be_performed"

            state.delete { action = "performed" }

            runCurrent()

            assertEquals(
                "Delete action not performed",
                "performed",
                action,
            )
        }

    private fun test(block: suspend TestScope.() -> Unit) =
        koinRunTest(
            before = { startTestKoin(modules = listOf(noteUiTestKoinModule())) },
            after = { stopTestKoin() },
            block = block,
        )
}
