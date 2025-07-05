package com.example.nodejstoapp

import com.example.nodejstoapp.model.Task
import com.example.nodejstoapp.ui.screen.Task.TaskRepository

class FakeTaskRepository : TaskRepository {

    private val tasks = mutableListOf<Task>()

    override suspend fun getTasks(): List<Task> {
        return tasks;
    }

    override suspend fun createTask(
        title: String,
        dueDate: String?
    ): Task {
        val newTask = Task(
            id = (tasks.size + 1).toString(),
            title = title,
            done = false,
            dueDate = dueDate,
            createdAt = "now"
        )
        tasks.add(newTask)
        return newTask
    }

    override suspend fun updateTask(
        id: String,
        title: String,
        done: Boolean
    ): Task {
        val index = tasks.indexOfFirst { it.id == id }
        if (index != -1) {
            val updated = tasks[index].copy(title = title, done = done)
            tasks[index] = updated
            return updated
        } else {
            throw IllegalArgumentException("Task not found")
        }
    }

    override suspend fun deleteTask(id: String) {
        tasks.removeIf { it.id == id }
    }

}