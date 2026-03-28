package eu.caraus.kmp.notes.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteDto)

    @Delete
    suspend fun delete(note: List<NoteDto>)

    @Query("DELETE FROM NoteDto")
    suspend fun deleteAll()

    @Query("DELETE FROM NoteDto WHERE id = :noteId")
    suspend fun deleteById(noteId: String)

    @Query("SELECT * FROM NoteDto WHERE id = :noteId")
    suspend fun findById(noteId: String): NoteDto?

    @Query("SELECT * FROM NoteDto")
    fun getAllAsFlow(): Flow<List<NoteDto>>
}
