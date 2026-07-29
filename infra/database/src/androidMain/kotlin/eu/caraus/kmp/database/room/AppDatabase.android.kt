package eu.caraus.kmp.database.room

import android.content.Context
import androidx.room3.Room
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Single

@Single
fun appDatabaseBuilder(ctx: Context): AppDatabase {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(AppDatabase.DATABASE_FILE_NAME)
    return Room
        .databaseBuilder<AppDatabase>(
            context = appContext,
            name = dbFile.absolutePath,
        ).fallbackToDestructiveMigration(false)
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
