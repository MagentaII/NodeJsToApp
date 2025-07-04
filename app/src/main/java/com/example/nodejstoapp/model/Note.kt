package com.example.nodejstoapp.model

import com.google.gson.annotations.SerializedName

data class Note(
    @SerializedName("_id")
    val id: String,
    val userId: String,
    val title: String,
    val content: String,
    val createdAt: String,
)
