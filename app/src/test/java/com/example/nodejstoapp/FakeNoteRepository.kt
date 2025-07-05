package com.example.nodejstoapp

import com.example.nodejstoapp.model.Note
import com.example.nodejstoapp.ui.screen.Note.NoteRepository
import okhttp3.ResponseBody

class FakeNoteRepository: NoteRepository {
    private val notes = mutableListOf<Note>()

    override suspend fun getNotes(): List<Note> {
        return notes
    }

    override suspend fun createNote(
        title: String,
        content: String
    ): Note {
        val note = Note(
            id = (notes.size + 1).toString(),
            userId = "685a5f1197dd5f24ce51d100",
            title = title,
            content = content,
            createdAt = "now"
        )
        notes.add(note)
        return note
    }

    override suspend fun updateNote(
        id: String,
        title: String,
        content: String
    ): Note {
        val updated = Note(id, "685a5f1197dd5f24ce51d100", title, content, "now")
        notes.replaceAll { if (it.id == id) updated else it }
        return updated
    }

    override suspend fun deleteNote(id: String) {
        notes.removeIf { it.id == id }
    }

}