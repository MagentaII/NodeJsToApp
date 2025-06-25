package com.example.nodejstoapp.network

import com.example.nodejstoapp.model.User
import com.example.nodejstoapp.model.AuthResponse
import com.example.nodejstoapp.model.Note
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Path

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

    @DELETE("/api/notes/{id}")
    suspend fun deleteNote(
        @Header("Authorization") token: String,
        @Path("id") noteId: Int
    ): ResponseBody
}