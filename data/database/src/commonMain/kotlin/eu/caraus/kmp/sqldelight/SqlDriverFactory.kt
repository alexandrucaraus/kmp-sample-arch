package eu.caraus.kmp.sqldelight

import app.cash.sqldelight.db.SqlDriver
import eu.caraus.kmp.database.sql.Database

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): Database {
    val driver = driverFactory.createDriver()
    val database = Database(driver)
    return database
}