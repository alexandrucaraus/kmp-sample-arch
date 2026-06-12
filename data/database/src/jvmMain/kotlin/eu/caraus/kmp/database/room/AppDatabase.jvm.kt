package eu.caraus.kmp.database.room

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Single
import java.io.File

@Single
fun appDatabaseBuilder(): AppDatabase {
    val dbFile = File(System.getProperty("user.home"), ".kmp_samplearch/${AppDatabase.DATABASE_FILE_NAME}")
    dbFile.parentFile?.mkdirs()
    return Room
        .databaseBuilder<AppDatabase>(name = dbFile.absolutePath)
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
