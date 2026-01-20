package eu.caraus.kmp.database.room

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@Single
actual fun appDatabaseBuilder(ctx: PlatformContextWrapper): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = documentDirectory() + "/${AppDatabase.DATABASE_FILE_NAME}"
    return Room
        .databaseBuilder<AppDatabase>(name = dbFilePath)
        .fallbackToDestructiveMigration(false)
        .setDriver(
            androidx.sqlite.driver.bundled
                .BundledSQLiteDriver(),
        ).setQueryCoroutineContext(Dispatchers.IO)
}

actual class PlatformContextWrapper

@Single
actual fun platformContextWrapper(scope: Scope): PlatformContextWrapper = PlatformContextWrapper()

@Single
actual fun appDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase = builder.build()

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory =
        NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
    return requireNotNull(documentDirectory?.path)
}
