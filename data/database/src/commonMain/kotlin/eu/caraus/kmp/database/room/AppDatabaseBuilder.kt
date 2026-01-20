@file:Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_MISMATCH")

package eu.caraus.kmp.database.room

import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Single
expect fun appDatabaseBuilder(ctx: PlatformContextWrapper): RoomDatabase.Builder<AppDatabase>

expect class PlatformContextWrapper

@Single
expect fun platformContextWrapper(scope: Scope): PlatformContextWrapper

@Single
expect fun appDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase

expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
