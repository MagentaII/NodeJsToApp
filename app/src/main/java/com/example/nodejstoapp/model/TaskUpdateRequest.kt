package com.example.nodejstoapp.model

data class TaskUpdateRequest(
    val title: String? = null,
    val done: Boolean? = null
)
