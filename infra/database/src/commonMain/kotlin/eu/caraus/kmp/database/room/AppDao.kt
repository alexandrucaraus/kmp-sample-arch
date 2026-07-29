package eu.caraus.kmp.database.room

import eu.caraus.kmp.notes.data.NoteDao
import org.koin.core.annotation.Single

@Single
fun provideNoteDao(appDatabase: AppDatabase): NoteDao = appDatabase.getNoteDao()
