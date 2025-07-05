package com.example.nodejstoapp.network

import com.example.nodejstoapp.model.AuthResponse
import com.example.nodejstoapp.model.Note
import com.example.nodejstoapp.model.Task
import com.example.nodejstoapp.model.TaskUpdateRequest
import com.example.nodejstoapp.model.User
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body request: Map<String, String>): AuthResponse

    @POST("/api/auth/register")
    suspend fun register(@Body request: Map<String, String>): ResponseBody

    @GET("/api/users")
    suspend fun getUsers(): List<User>

    @GET("/api/notes")
    suspend fun getNotes(): List<Note>

    @POST("/api/notes")
    suspend fun createNote(@Body note: Map<String, String>): Note

    @PUT("/api/notes/{id}")
    suspend fun updateNote(@Path("id") noteId: String, @Body note: Map<String, String>): Note

    @DELETE("/api/notes/{id}")
    suspend fun deleteNote(@Path("id") noteId: String)

    @GET("/api/tasks")
    suspend fun getTasks(): List<Task>

    @POST("/api/tasks")
    suspend fun createTask(@Body task: Map<String, String?>): Task

    @PUT("/api/tasks/{id}")
    suspend fun updateTask(@Path("id") taskId: String, @Body task: TaskUpdateRequest): Task

    @DELETE("/api/tasks/{id}")
    suspend fun deleteTask(@Path("id") taskId: String): ResponseBody

}