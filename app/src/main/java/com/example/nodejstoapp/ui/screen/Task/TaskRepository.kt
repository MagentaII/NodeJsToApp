package com.example.nodejstoapp.ui.screen.Task

import com.example.nodejstoapp.model.Task
import com.example.nodejstoapp.model.TaskUpdateRequest
import com.example.nodejstoapp.network.ApiService

interface TaskRepository {
    suspend fun getTasks(): List<Task>
    suspend fun createTask(title: String, dueDate: String?): Task
    suspend fun updateTask(id: String, title: String, done: Boolean): Task
    suspend fun deleteTask(id: String)
}

class TaskRepositoryImpl(private val api: ApiService) : TaskRepository {
    override suspend fun getTasks(): List<Task> = api.getTasks()

    override suspend fun createTask(title: String, dueDate: String?): Task {
        return api.createTask(
            mapOf("title" to title, "dueDate" to dueDate)
        )
    }

    override suspend fun updateTask(id: String, title: String, done: Boolean): Task {
        return api.updateTask(id, TaskUpdateRequest(title, done))
    }

    override suspend fun deleteTask(id: String) {
        api.deleteTask(id)
    }
}