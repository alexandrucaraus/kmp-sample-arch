package eu.caraus.kmp.samplearch

import android.content.Context
import eu.caraus.kmp.database.getDatabaseBuilder
import eu.caraus.kmp.notes.NoteFactory

fun setupDi(applicationContext: Context) {
    NoteFactory.setupAppDatabase(getDatabaseBuilder(applicationContext).build())
}