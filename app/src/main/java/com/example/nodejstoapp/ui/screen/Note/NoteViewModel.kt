package com.example.nodejstoapp.ui.screen.Note

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nodejstoapp.model.Note
import com.example.nodejstoapp.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteViewModel(
    private val repository: NoteRepository
): ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadNotes() {
        viewModelScope.launch {
            try {
                _notes.value = repository.getNotes()
            } catch (e: Exception) {
                Log.e("NoteViewModel", "載入失敗：${e.message}")
                _error.value = "載入失敗：${e.message}"
            }
        }
    }

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            try {
                val newNote = repository.createNote(title, content)
                _notes.value = listOf(newNote) + _notes.value
            } catch (e: Exception) {
                Log.e("NoteViewModel", "新增失敗：${e.message}")
                _error.value = "新增失敗：${e.message}"
            }
        }
    }

    fun updateNote(id: String, title: String, content: String) {
        viewModelScope.launch {
            try {
                val updatedNote = repository.updateNote(id, title, content)
                _notes.value = _notes.value.map {
                    if (it.id == id) updatedNote else it
                }
            } catch (e: Exception) {
                Log.e("NoteViewModel", "更新失敗：${e.message}")
                _error.value = "更新失敗：${e.message}"
            }
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            try {
                repository.deleteNote(noteId)
                _notes.value = _notes.value.filterNot { it.id == noteId }
            } catch (e: Exception) {
                Log.e("NoteViewModel", "刪除失敗：${e.message}")
                _error.value = "刪除失敗：${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}