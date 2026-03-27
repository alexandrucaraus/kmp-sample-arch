package eu.caraus.kmp.database.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabaseConstructor
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent

//actual object AppDatabaseConstructor :
//    RoomDatabaseConstructor<AppDatabase> {
//    actual override fun initialize(): AppDatabase = //AppDatabase_Impl()
//        throw NotImplementedError("Room KSP should have handled this")
//    //       (object : KoinComponent {}).getKoin().get()
//}

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
