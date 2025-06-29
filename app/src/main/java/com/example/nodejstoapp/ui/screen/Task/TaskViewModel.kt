package com.example.nodejstoapp.ui.screen.Task

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nodejstoapp.model.Task
import com.example.nodejstoapp.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository): ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _error = MutableStateFlow<String?>(null);
    val error: StateFlow<String?> = _error

    fun loadTasks() {
        viewModelScope.launch {
            try {
                _tasks.value = repository.getTasks()
            } catch (e: Exception) {
                Log.e("TaskViewModel", "任務載入失敗: ${e.message}")
                _error.value = "任務載入失敗: ${e.message}"
            }
        }
    }

    fun createTask(title: String, dueDate: String) {
        viewModelScope.launch {
            try {
                val newTask = repository.createTask(title, dueDate)
                _tasks.value = listOf(newTask) + _tasks.value
            } catch (e: Exception) {
                Log.e("TaskViewModel", "任務新增失敗: ${e.message}")
                _error.value = "任務新增失敗: ${e.message}"
            }
        }
    }

    fun updateTask(id: String, title: String, done: Boolean) {
        viewModelScope.launch {
            try {
                val updatedTask = repository.updateTask(id, title, done)
                _tasks.value = _tasks.value.map {
                    if (it.id == id) updatedTask else it
                }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "任務更新失敗: ${e.message}")
                _error.value = "任務更新失敗: ${e.message}"
            }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                repository.deleteTask(taskId)
                _tasks.value = _tasks.value.filterNot { it.id == taskId }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "任務刪除失敗: ${e.message}")
                _error.value = "任務刪除失敗: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}