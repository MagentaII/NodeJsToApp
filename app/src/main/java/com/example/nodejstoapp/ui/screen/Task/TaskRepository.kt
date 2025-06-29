package com.example.nodejstoapp.ui.screen.Task

import com.example.nodejstoapp.model.Task
import com.example.nodejstoapp.model.TaskUpdateRequest
import com.example.nodejstoapp.network.ApiService

class TaskRepository(private val api: ApiService) {
    suspend fun getTasks(): List<Task> = api.getTasks()

    suspend fun createTask(title: String, dueDate: String): Task {
        return api.createTask(
            mapOf("title" to title, "dueDate" to dueDate)
        )
    }

    suspend fun updateTask(id: String, title: String, done: Boolean): Task {
        return api.updateTask(id, TaskUpdateRequest(title, done))
    }

    suspend fun deleteTask(id: String) {
        api.deleteTask(id)
    }
}