package com.example.nodejstoapp.ui.screen.Note

import com.example.nodejstoapp.model.Note
import com.example.nodejstoapp.network.ApiService

class NoteRepository(private val api: ApiService) {
    suspend fun getNotes(): List<Note> = api.getNotes()
    suspend fun createNote(title: String, content: String): Note =
        api.createNote( mapOf("title" to title, "content" to content))
    suspend fun updateNote(id: String, title: String, content: String): Note =
        api.updateNote(id, mapOf("title" to title, "content" to content))
    suspend fun deleteNote(id: String) = api.deleteNote( id)
}