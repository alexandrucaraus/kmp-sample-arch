package eu.caraus.kmp.database.room

import androidx.room3.Ignore
import androidx.room3.testing.MigrationTestHelper
import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {
    private val migrationTestDB = "migration-test"

    @get:Rule
    val migrationTestHelper =
        MigrationTestHelper(
            instrumentation = InstrumentationRegistry.getInstrumentation(),
            file = InstrumentationRegistry.getInstrumentation().targetContext.getDatabasePath(migrationTestDB),
            driver = BundledSQLiteDriver(),
            databaseClass = AppDatabase::class,
        )

    @Test
    fun validateSchema() {
        runBlocking {
            migrationTestHelper.createDatabase(version = 1).apply { close() }
        }
    }

//    @Test
//    @Ignore // only version 1 exist atm
//    fun migrate_1_to_2() { runBlocking {
//        migrationTestHelper.createDatabase( 1).apply { close() }
// //         schema ver 1 exists atm
// //        migrationTestHelper.runMigrationsAndValidate(
// //            MIGRATION_TEST_DB,
// //            2,
// //            true,
// //            MIGRATION_1_2
// //        )
//    }}
}
