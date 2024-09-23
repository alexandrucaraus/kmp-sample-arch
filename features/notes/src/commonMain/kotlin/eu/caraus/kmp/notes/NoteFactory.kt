@file:Suppress("UNCHECKED_CAST")

package eu.caraus.kmp.notes

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.caraus.kmp.database.AppDatabase
import eu.caraus.kmp.notes.data.NoteRepositoryInMem
import eu.caraus.kmp.notes.data.NoteRepositoryRoom
import eu.caraus.kmp.notes.domain.CreateNoteUseCase
import eu.caraus.kmp.notes.domain.DeleteNoteUseCase
import eu.caraus.kmp.notes.domain.GetNoteUseCase
import eu.caraus.kmp.notes.domain.GetNotesListUseCase
import eu.caraus.kmp.notes.domain.SaveNoteUseCase
import eu.caraus.kmp.notes.domain.UpdateNoteUseCase
import eu.caraus.kmp.notes.presentation.details.NoteDetailsViewModel
import eu.caraus.kmp.notes.presentation.list.NoteListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.reflect.KClass

object NoteFactory {

    private var db: AppDatabase? = null
    fun setupAppDatabase(db: AppDatabase) {
        this.db = db
    }

    private val noteDao by lazy { db?.getNoteDao() ?: throw IllegalStateException("Database not set") }

    private val repository by lazy {
        NoteRepositoryRoom(noteDao)
    }

    private fun getNoteListUseCase() = GetNotesListUseCase(repository)

    private fun getNoteUseCase() = GetNoteUseCase(repository)

    private fun saveNoteUseCase() = SaveNoteUseCase(updateNoteUseCase(), createNoteUseCase())

    private fun createNoteUseCase() = CreateNoteUseCase(repository)

    private fun updateNoteUseCase() = UpdateNoteUseCase(repository)

    private fun deleteNoteUseCase() = DeleteNoteUseCase(repository)

    private fun createViewModelCoroutineScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    fun createNoteListViewModel(
        coroutineScope: CoroutineScope = createViewModelCoroutineScope(),
    ) = NoteListViewModel(
        notesList = getNoteListUseCase(),
        deleteUseCase = deleteNoteUseCase(),
        scope = coroutineScope,
    )

    fun createNoteDetailsViewModel(
        noteId: String,
        coroutineScope: CoroutineScope = createViewModelCoroutineScope(),
    ) = NoteDetailsViewModel(
        noteId = noteId,
        getNote = getNoteUseCase(),
        saveNote = saveNoteUseCase(),
        deleteNote = deleteNoteUseCase(),
        coroutineScope = coroutineScope,
    )

}

@Composable
inline fun <reified T: ViewModel> viewModelOf(noinline block: () -> T) = viewModel<T>(
    factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            return block() as T
        }
    }
)
