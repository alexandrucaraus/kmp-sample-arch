package eu.caraus.kmp.samplearch

import eu.caraus.kmp.database.getDatabaseBuilder
import eu.caraus.kmp.notes.NoteFactory

fun setupDi() {
    NoteFactory.setupAppDatabase(getDatabaseBuilder().build())
}