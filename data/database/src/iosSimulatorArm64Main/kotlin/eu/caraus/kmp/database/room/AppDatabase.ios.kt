package eu.caraus.kmp.database.room

import androidx.room.Room
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.annotation.Single
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@Single
fun appDatabaseBuilder(): AppDatabase {
    val dbFilePath = documentDirectory() + "/${AppDatabase.DATABASE_FILE_NAME}"

    return Room
        .databaseBuilder<AppDatabase>(name = dbFilePath)
        .fallbackToDestructiveMigration(false)
        .setDriver(
            androidx.sqlite.driver.bundled
                .BundledSQLiteDriver(),
        ).setQueryCoroutineContext(Dispatchers.IO).build()
}

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
