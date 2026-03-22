@file:Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_MISMATCH")

package eu.caraus.kmp.database.room

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Single
import org.koin.mp.KoinPlatform.getKoin

@Single
actual fun appDatabaseBuilder(ctx: PlatformContextWrapper): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.androidContext.applicationContext
    val dbFile = appContext.getDatabasePath(AppDatabase.DATABASE_FILE_NAME)
    return Room
        .databaseBuilder<AppDatabase>(
            context = appContext,
            name = dbFile.absolutePath,
        ).fallbackToDestructiveMigration(false)
        .setQueryCoroutineContext(Dispatchers.IO)
}

@Single
actual class PlatformContextWrapper(
    val androidContext: Context,
)

@Single
actual fun platformContextWrapper(
 //   scope: Scope
): PlatformContextWrapper {
    val context: Application = getKoin().get()
    return PlatformContextWrapper(context)
}

@Single
actual fun appDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase = builder.build()
