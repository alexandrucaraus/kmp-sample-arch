package eu.caraus.kmp.database.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.core.scope.Scope


actual class ContextWrapper(val ctx: Context)

actual fun providesContextWrapper(scope: Scope) : ContextWrapper =
    ContextWrapper(scope.get())

//@Single
actual fun appDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase = builder.build()

//@Single
actual fun appDatabaseBuilder(ctx: ContextWrapper): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.ctx.applicationContext
    val dbFile = appContext.getDatabasePath("notes_room.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
        .fallbackToDestructiveMigration(false)
        .setQueryCoroutineContext(Dispatchers.IO)
}
