package eu.caraus.kmp.database.room

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import eu.caraus.kmp.notes.data.NoteDao
import eu.caraus.kmp.notes.data.NoteDto

val AppDatabase.Companion.DATABASE_FILE_NAME: String get() = "notes_room.db"

@Suppress("NO_ACTUAL_FOR_EXPECT")
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

@Suppress("KotlinNoActualForExpect", "NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
