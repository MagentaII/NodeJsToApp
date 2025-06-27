package com.example.nodejstoapp.model

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("_id")
    val id: String,
    val title: String,
    val done: Boolean,
    val dueDate: String?,
    val createdAt: String,
)
