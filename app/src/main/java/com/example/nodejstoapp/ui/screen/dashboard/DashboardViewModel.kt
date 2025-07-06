package com.example.nodejstoapp.ui.screen.dashboard

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nodejstoapp.network.ApiClient
import com.example.nodejstoapp.sp.SessionManager
import com.example.nodejstoapp.ui.screen.Note.NoteRepository
import com.example.nodejstoapp.ui.screen.Note.NoteRepositoryImpl
import com.example.nodejstoapp.ui.screen.Task.TaskRepository
import com.example.nodejstoapp.ui.screen.Task.TaskRepositoryImpl
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val context: Context,
    private val noteRepository: NoteRepository = NoteRepositoryImpl(ApiClient.api),
    private val taskRepository: TaskRepository = TaskRepositoryImpl(ApiClient.api)
) : ViewModel() {

    var noteCount by mutableStateOf(0)
        private set

    var pendingTaskCount by mutableStateOf(0)
        private set

    var userEmail by mutableStateOf("未登入")
        private set

    init {
        viewModelScope.launch {
            val token = SessionManager.getAccessToken(context)
            if (token != null) {
                userEmail = "未知"
                try {
                    val notes = noteRepository.getNotes()
                    val tasks = taskRepository.getTasks()

                    noteCount = notes.size
                    pendingTaskCount = tasks.count { !it.done }
                } catch (e: Exception) {
                    Log.e("DASHBOARD", "載入失敗：${e.message}")
                }
            }
        }
    }
}