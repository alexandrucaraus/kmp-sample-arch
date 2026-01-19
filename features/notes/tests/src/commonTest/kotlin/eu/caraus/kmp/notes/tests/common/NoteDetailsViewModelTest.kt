@file:OptIn(ExperimentalCoroutinesApi::class)

package eu.caraus.kmp.notes.tests.common

import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.NoteRepository
import eu.caraus.kmp.notes.domain.NoteRepositoryInMem
import eu.caraus.kmp.notes.ui.details.NoteDetailsViewModel
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
            state.updateContent("updatedNote3Content")

            runCurrent()

            assertEquals(
                "updateNote3Title",
                vm.state.value.title,
                "Title did not change",
            )
            assertEquals(
                "updatedNote3Content",
                vm.state.value.content,
                "Content did not change",
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
                "updateNote3Title",
                vm.state.value.title,
                "Title did not change",
            )
            assertEquals(
                "updatedNote3Content",
                vm.state.value.content,
                "Content did not change",
            )

            var action = "to_be_performed"

            state.delete { action = "performed" }

            runCurrent()

            assertEquals(
                "performed",
                action,
                "Delete action not performed",
            )
        }

    private fun test(block: suspend TestScope.() -> Unit) =
        koinRunTest(
            before = { startTestKoin(modules = listOf(commonTestModules())) },
            after = { stopTestKoin() },
            block = block,
        )
}
