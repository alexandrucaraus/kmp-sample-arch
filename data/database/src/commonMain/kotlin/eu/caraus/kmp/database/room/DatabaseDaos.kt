package eu.caraus.kmp.database.room

import org.koin.core.annotation.Single

@Single
fun provideNoteDao(appDatabase: AppDatabase): NoteDao = appDatabase.getNoteDao()
