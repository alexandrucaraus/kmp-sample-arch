package eu.caraus.kmp.notes.ui.list

import eu.caraus.kmp.notes.domain.Note
import eu.caraus.kmp.notes.domain.NoteDiModule
import eu.caraus.kmp.notes.domain.NoteId
import eu.caraus.kmp.notes.domain.NoteRepository
import eu.caraus.kmp.notes.ui.NoteUiDiModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.mock.declare
import org.koin.test.mock.declareMock
import kotlin.test.Test

class NoteListViewModelTest : KoinTest {

    @Test
    fun list_notes_on_init(): Unit = runTest {

        startKoin {
            modules(
//                NoteDiModule().module,
//                NoteUiDiModule().module
            )
        }

//        declare {
//            single<NoteRepository> {
//                object : NoteRepository {
//                    override suspend fun save(note: Note) {
//                        TODO("Not yet implemented")
//                    }
//
//                    override suspend fun delete(note: Note) {
//                        TODO("Not yet implemented")
//                    }
//
//                    override suspend fun delete(notes: List<Note>) {
//                        TODO("Not yet implemented")
//                    }
//
//                    override suspend fun deleteById(noteId: NoteId) {
//                        TODO("Not yet implemented")
//                    }
//
//                    override suspend fun findById(noteId: NoteId): Note? {
//                        TODO("Not yet implemented")
//                    }
//
//                    override suspend fun findAll(): List<Note> {
//                        TODO("Not yet implemented")
//                    }
//
//                    override fun allAsFlow(): Flow<List<Note>> {
//                        TODO("Not yet implemented")
//                    }
//
//                }
//            }
//        }

    }

    @Test
    fun select_multiple_notes() {
    }

    @Test
    fun delete_selected_notes() {
    }
}
