package eu.caraus.kmp.database.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.core.scope.Scope

actual class PlatformContextWrapper(val ctx: Context)

actual fun platformContextWrapper(scope: Scope) : PlatformContextWrapper =
    PlatformContextWrapper(scope.get())

actual fun appDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase = builder.build()

actual fun appDatabaseBuilder(ctx: PlatformContextWrapper): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.ctx.applicationContext
    val dbFile = appContext.getDatabasePath(DATABASE_FILE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
        .fallbackToDestructiveMigration(false)
        .setQueryCoroutineContext(Dispatchers.IO)
}
