package eu.caraus.kmp.database.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase

const val DATABASE_FILE_NAME = "notes_room.db"

@Database(
    version = 1,
    entities = [
        NoteDto::class
    ],
    exportSchema = true,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao
}
