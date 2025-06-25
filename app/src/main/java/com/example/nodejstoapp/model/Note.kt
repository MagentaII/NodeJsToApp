package com.example.nodejstoapp.model

data class Note(
    val id: Int,
    val UserId: Int,
    val title: String,
    val content: String,
    val createdAt: String,
)
