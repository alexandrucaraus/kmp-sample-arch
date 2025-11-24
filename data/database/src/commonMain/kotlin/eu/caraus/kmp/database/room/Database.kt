package eu.caraus.kmp.database.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import org.koin.core.annotation.Module
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Single

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

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

@Single
expect fun appDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase

@Single
expect fun appDatabaseBuilder(ctx: ContextWrapper): RoomDatabase.Builder<AppDatabase>

expect class ContextWrapper

@Single
expect fun providesContextWrapper(scope: org.koin.core.scope.Scope) : ContextWrapper
