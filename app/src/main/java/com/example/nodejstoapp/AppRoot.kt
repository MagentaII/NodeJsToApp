package com.example.nodejstoapp

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.nodejstoapp.ui.screen.Login.LoginScreen
import com.example.nodejstoapp.ui.screen.Note.NoteListScreen
import com.example.nodejstoapp.ui.screen.Register.RegisterScreen
import com.example.nodejstoapp.ui.screen.Task.TaskListScreen

enum class Screen {
    NOTES, TASKS
}

@Composable
fun AppRoot(context: Context, modifier: Modifier = Modifier) {
    var accessToken by remember { mutableStateOf<String?>(null) }
    var refreshToken by remember { mutableStateOf<String?>(null) }
    var currentScreen by remember { mutableStateOf(Screen.NOTES) }
    var isRegistering by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        accessToken = SessionManager.getAccessToken(context)
        refreshToken = SessionManager.getRefreshToken(context)
    }

    when {
        isRegistering -> {
            RegisterScreen(
                onBackToLogin = { isRegistering = false },
                modifier = modifier
            )
        }

        accessToken.isNullOrEmpty() -> {
            LoginScreen(
                onLoginSuccess = { access, refresh ->
                    accessToken = access
                    refreshToken = refresh
                    SessionManager.saveTokens(context, access, refresh )
                                 },
                onGoToRegister = { isRegistering = true },
                modifier = modifier
            )
        }

        else -> {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            selected = currentScreen == Screen.NOTES,
                            onClick = { currentScreen = Screen.NOTES },
                            icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                            label = { Text("Notes") }
                        )
                        NavigationBarItem(
                            selected = currentScreen == Screen.TASKS,
                            onClick = { currentScreen = Screen.TASKS },
                            icon = { Icon(Icons.Default.Check, contentDescription = null) },
                            label = { Text("Tasks") }
                        )
                    }
                }
            ) { innerPadding ->
                when (currentScreen) {
                    Screen.NOTES -> NoteListScreen(
                        onLogout = {
                            accessToken = null
                            refreshToken = null
                            SessionManager.clearTokens(context)
                                   },
                        modifier = modifier.padding(innerPadding)
                    )

                    Screen.TASKS -> TaskListScreen(
                        onLogout = {
                            accessToken = null
                            refreshToken = null
                            SessionManager.clearTokens(context)
                                   },
                        modifier = modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}