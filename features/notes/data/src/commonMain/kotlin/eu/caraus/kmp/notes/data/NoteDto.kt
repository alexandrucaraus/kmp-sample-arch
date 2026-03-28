package eu.caraus.kmp.notes.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity
@OptIn(ExperimentalUuidApi::class)
data class NoteDto(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long,
)
