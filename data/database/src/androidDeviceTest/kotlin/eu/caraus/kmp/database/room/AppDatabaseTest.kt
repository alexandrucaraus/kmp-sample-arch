package eu.caraus.kmp.database.room

import androidx.room.Ignore
import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private val migrationTestDB = "migration-test"

    @get:Rule
    val migrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java
    )

    @Test
    fun validateSchema() {
        migrationTestHelper.createDatabase(
            migrationTestDB, AppDatabase.
            LATEST_VERSION
        ).apply {
            close()
        }

        migrationTestHelper.runMigrationsAndValidate(
            migrationTestDB,
            AppDatabase.LATEST_VERSION,
            true,
            *arrayOf()
        )
    }

    @Test
    @Ignore // only version 1 exist atm
    fun migrate_1_to_2() {
        migrationTestHelper.createDatabase(migrationTestDB, 1).apply {
            close()
        }
        // schema ver 1 exists atm
//        migrationTestHelper.runMigrationsAndValidate(
//            MIGRATION_TEST_DB,
//            2,
//            true,
//            MIGRATION_1_2
//        )
    }
}
