package eu.caraus.kmp.database

import org.koin.core.module.Module
import org.koin.dsl.module

actual fun nativeDatabaseModules() : List<Module> = listOf(
    module {
        single<AppDatabase> {  getDatabaseBuilder(get()).build() }
        single<NoteDao> { get<AppDatabase>().getNoteDao() }
    }
)