@file:OptIn(ExperimentalCoroutinesApi::class)

package eu.caraus.kmp.notes.ui.list

import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.koin.test.KoinTest
import kotlin.test.Test
import kotlin.test.assertTrue
import org.koin.test.inject
import org.koin.test.mock.declare

class NoteListViewModelTest : KoinTest {

    @Test
    fun list_notes_on_init() = runTest {
        startTestKoin()
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
    fun select_multiple_notes() {
    }

    @Test
    fun delete_selected_notes() {
    }
}
