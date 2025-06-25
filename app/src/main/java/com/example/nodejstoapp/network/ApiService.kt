package com.example.nodejstoapp.network

import com.example.nodejstoapp.model.User
import com.example.nodejstoapp.model.AuthResponse
import com.example.nodejstoapp.model.Note
import com.example.nodejstoapp.model.Task
import com.example.nodejstoapp.model.TaskUpdateRequest
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.http.PUT

interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body request: Map<String, String>): AuthResponse

    @POST("/api/auth/register")
    suspend fun register(
        @Body request: Map<String, String>
    ): ResponseBody

    @GET("/api/users")
    suspend fun getUsers(@Header("Authorization") token: String): List<User>

    @GET("/api/notes")
    suspend fun getNotes(@Header("Authorization") token: String): List<Note>

    @POST("/api/notes")
    suspend fun createNote(
        @Header("Authorization") token: String,
        @Body note: Map<String, String>
    ): Note

    @PUT("/api/notes/{id}")
    suspend fun updateNote(
        @Header("Authorization") token: String,
        @Path("id") noteId: String,
        @Body note: Map<String, String>
    ): Note

    @DELETE("/api/notes/{id}")
    suspend fun deleteNote(
        @Header("Authorization") token: String,
        @Path("id") noteId: String
    ): ResponseBody

    @GET("/api/tasks")
    suspend fun getTasks(@Header("Authorization") token: String): List<Task>

    @POST("/api/tasks")
    suspend fun createTask(
        @Header("Authorization") token: String,
        @Body task: Map<String, String>
    ): Task

    @PUT("/api/tasks/{id}")
    suspend fun updateTask(
        @Header("Authorization") token: String,
        @Path("id") taskId: String,
        @Body task: TaskUpdateRequest
    ): Task

    @DELETE("/api/tasks/{id}")
    suspend fun deleteTask(
        @Header("Authorization") token: String,
        @Path("id") taskId: String
    ): ResponseBody

}