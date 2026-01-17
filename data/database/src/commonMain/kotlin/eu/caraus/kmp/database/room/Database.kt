package eu.caraus.kmp.database.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import eu.caraus.kmp.notes.data.NoteDao
import eu.caraus.kmp.notes.data.NoteDto

@Database(
    version = AppDatabase.LATEST_VERSION,
    entities = [
        NoteDto::class,
    ],
    exportSchema = true,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao

    companion object {
        const val LATEST_VERSION = 1
    }
}

val AppDatabase.Companion.DATABASE_FILE_NAME: String get() = "notes_room.db"
