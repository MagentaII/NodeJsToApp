package com.example.nodejstoapp.ui.screen.Note

import com.example.nodejstoapp.model.Note
import com.example.nodejstoapp.network.ApiService
import okhttp3.ResponseBody

interface NoteRepository {
    suspend fun getNotes(): List<Note>
    suspend fun createNote(title: String, content: String): Note
    suspend fun updateNote(id: String, title: String, content: String): Note
    suspend fun deleteNote(id: String)
}

class NoteRepositoryImpl(private val api: ApiService): NoteRepository {
    override suspend fun getNotes(): List<Note> = api.getNotes()
    override suspend fun createNote(title: String, content: String): Note =
        api.createNote( mapOf("title" to title, "content" to content))
    override suspend fun updateNote(id: String, title: String, content: String): Note =
        api.updateNote(id, mapOf("title" to title, "content" to content))
    override suspend fun deleteNote(id: String) = api.deleteNote( id)
}