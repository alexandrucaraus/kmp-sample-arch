@file:Suppress( "NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_MISMATCH")
package eu.caraus.kmp.database.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope


@Single
actual class PlatformContextWrapper(val androidContext: Context)


@Single
actual fun platformContextWrapper(scope: Scope) : PlatformContextWrapper =
    PlatformContextWrapper(scope.get())

@Single
actual fun appDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase = builder.build()

@Single
actual fun appDatabaseBuilder(ctx: PlatformContextWrapper): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.androidContext.applicationContext
    val dbFile = appContext.getDatabasePath(DATABASE_FILE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
        .fallbackToDestructiveMigration(false)
        .setQueryCoroutineContext(Dispatchers.IO)
}
