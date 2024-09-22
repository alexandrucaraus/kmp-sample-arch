package eu.caraus.kmp.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity
data class NoteDto @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long,
)

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteDto)

    @Delete
    suspend fun delete(note: NoteDto)

    @Delete
    suspend fun delete(vararg note: NoteDto)

    @Query("DELETE FROM NoteDto WHERE id = :noteId")
    suspend fun deleteById(noteId: String)

    @Query("SELECT * FROM NoteDto WHERE id = :noteId")
    suspend fun findById(noteId: String): NoteDto?

    @Query("SELECT * FROM NoteDto")
    fun getAllAsFlow(): Flow<List<NoteDto>>

}

