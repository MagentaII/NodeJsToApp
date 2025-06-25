package com.example.nodejstoapp.ui.screen.Task

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.nodejstoapp.model.Task
import com.example.nodejstoapp.model.TaskUpdateRequest
import com.example.nodejstoapp.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TaskListScreen(token: String, onLogout: () -> Unit, modifier: Modifier = Modifier) {
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var newTaskTitle by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        try {
            tasks = ApiClient.api.getTasks("Bearer $token")
        } catch (e: Exception) {
            Log.e("TaskListScreen", "Error loading tasks", e)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            Button(onClick = onLogout) {
                Text("登出")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("任務清單", style = MaterialTheme.typography.headlineSmall)

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newTaskTitle,
                onValueChange = { newTaskTitle = it },
                label = { Text("新增任務") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (newTaskTitle.isNotBlank()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val newTask = ApiClient.api.createTask(
                                "Bearer $token",
                                mapOf("title" to newTaskTitle)
                            )
                            tasks = listOf(newTask) + tasks
                            newTaskTitle = ""
                        } catch (e: Exception) {
                            Log.e("TASK_CREATE", e.message ?: "")
                        }
                    }
                }
            }) {
                Text("新增")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks) { task ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = task.done,
                        onCheckedChange = { checked ->
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val updated = ApiClient.api.updateTask(
                                        "Bearer $token",
                                        task.id,
                                        TaskUpdateRequest(title = task.title, done = checked)
                                    )
                                    tasks = tasks.map { if (it.id == task.id) updated else it }
                                } catch (e: Exception) {
                                    Log.e("TASK_UPDATE", e.message ?: "")
                                }
                            }
                        }
                    )
                    Text(
                        text = task.title,
                        modifier = Modifier.weight(1f),
                        style = if (task.done)
                            MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.LineThrough)
                        else
                            MaterialTheme.typography.bodyLarge
                    )
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                ApiClient.api.deleteTask("Bearer $token", task.id)
                                tasks = tasks.filterNot { it.id == task.id }
                            } catch (e: Exception) {
                                Log.e("TASK_DELETE", e.message ?: "")
                            }
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                    }
                }
            }
        }

    }
}