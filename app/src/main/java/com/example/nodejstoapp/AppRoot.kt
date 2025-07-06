package com.example.nodejstoapp

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nodejstoapp.sp.SessionManager
import com.example.nodejstoapp.ui.screen.Login.LoginScreen
import com.example.nodejstoapp.ui.screen.Note.NoteListScreen
import com.example.nodejstoapp.ui.screen.Register.RegisterScreen
import com.example.nodejstoapp.ui.screen.Task.TaskListScreen
import com.example.nodejstoapp.ui.screen.dashboard.DashboardScreen

enum class Screen {
    NOTES, TASKS
}

@Composable
fun AppRoot(context: Context, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val accessToken = remember { mutableStateOf(SessionManager.getAccessToken(context)) }
    val refreshToken = remember { mutableStateOf(SessionManager.getRefreshToken(context)) }
    val isRegistering = remember { mutableStateOf(false) }

//    var accessToken by remember { mutableStateOf<String?>(null) }
//    var refreshToken by remember { mutableStateOf<String?>(null) }
//    var currentScreen by remember { mutableStateOf(Screen.NOTES) }
//    var isRegistering by remember { mutableStateOf(false) }

//    LaunchedEffect(Unit) {
//        accessToken = SessionManager.getAccessToken(context)
//        refreshToken = SessionManager.getRefreshToken(context)
//    }

    when {
        isRegistering.value -> {
            RegisterScreen(
                onBackToLogin = { isRegistering.value = false },
                modifier = modifier
            )
        }

        accessToken.value.isNullOrEmpty() -> {
            LoginScreen(
                onLoginSuccess = { access, refresh ->
                    accessToken.value = access
                    refreshToken.value = refresh
                    SessionManager.saveTokens(context, access, refresh )
                                 },
                onGoToRegister = { isRegistering.value = true },
                modifier = modifier
            )
        }

        else -> {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        listOf("dashboard", "notes", "tasks").forEach { route ->
                            val icon = when (route) {
                                "dashboard" -> Icons.Default.Home
                                "notes" -> Icons.Default.Edit
                                "tasks" -> Icons.Default.Check
                                else -> Icons.Default.Home
                            }
                            NavigationBarItem(
                                icon = { Icon(icon, contentDescription = null) },
                                label = { Text(route.replaceFirstChar { it.uppercase() }) },
                                selected = navController.currentDestination?.route == route,
                                onClick = { navController.navigate(route) }
                            )
                        }
//                        NavigationBarItem(
//                            icon = { Icon(Icons.Default.Home, contentDescription = "總覽") },
//                            label = { Text("總覽") },
//                            selected = currentRoute == "dashboard",
//                            onClick = { navController.navigate("dashboard") }
//                        )
//                        NavigationBarItem(
//                            selected = currentScreen == Screen.NOTES,
//                            onClick = { currentScreen = Screen.NOTES },
//                            icon = { Icon(Icons.Default.Edit, contentDescription = null) },
//                            label = { Text("Notes") }
//                        )
//                        NavigationBarItem(
//                            selected = currentScreen == Screen.TASKS,
//                            onClick = { currentScreen = Screen.TASKS },
//                            icon = { Icon(Icons.Default.Check, contentDescription = null) },
//                            label = { Text("Tasks") }
//                        )
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "dashboard",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("dashboard") {
                        DashboardScreen(context) // 後續你再實作
                    }
                    composable("notes") {
                        NoteListScreen(
                            onLogout = {
                                SessionManager.clearTokens(context)
                                accessToken.value = null
                            }
                        )
                    }
                    composable("tasks") {
                        TaskListScreen(
                            onLogout = {
                                SessionManager.clearTokens(context)
                                accessToken.value = null
                            }
                        )
                    }
                }
//                when (currentScreen) {
//                    Screen.NOTES -> NoteListScreen(
//                        onLogout = {
//                            accessToken = null
//                            refreshToken = null
//                            SessionManager.clearTokens(context)
//                                   },
//                        modifier = modifier.padding(innerPadding)
//                    )
//
//                    Screen.TASKS -> TaskListScreen(
//                        onLogout = {
//                            accessToken = null
//                            refreshToken = null
//                            SessionManager.clearTokens(context)
//                                   },
//                        modifier = modifier.padding(innerPadding)
//                    )
//                }
            }
        }
    }
}