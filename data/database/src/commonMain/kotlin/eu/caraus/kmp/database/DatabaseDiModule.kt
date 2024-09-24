package eu.caraus.kmp.database

import org.koin.core.module.Module

val databaseDiModules =  nativeDatabaseModules()

expect fun nativeDatabaseModules(): List<Module>
