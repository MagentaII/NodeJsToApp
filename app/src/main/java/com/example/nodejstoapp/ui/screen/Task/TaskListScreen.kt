package com.example.nodejstoapp.ui.screen.Task

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nodejstoapp.model.Task
import com.example.nodejstoapp.model.TaskUpdateRequest
import com.example.nodejstoapp.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TaskListScreen(onLogout: () -> Unit, modifier: Modifier = Modifier) {
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var newTaskTitle by remember { mutableStateOf("") }
    var dueDateText by remember { mutableStateOf("") }

    var editingTaskId by remember { mutableStateOf<String?>(null) }
    var editingText by remember { mutableStateOf("") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            dueDateText = "%04d-%02d-%02d".format(year, month + 1, dayOfMonth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    LaunchedEffect(Unit) {
        try {
            tasks = ApiClient.api.getTasks()
        } catch (e: Exception) {
            Log.e("TaskListScreen", "Error loading tasks", e)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("任務清單", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
            Button(onClick = onLogout) {
                Text("登出")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 任務輸入區
        Column {
            OutlinedTextField(
                value = newTaskTitle,
                onValueChange = { newTaskTitle = it },
                label = { Text("任務標題") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("截止日：", modifier = Modifier.padding(end = 8.dp))
                Button(onClick = { datePickerDialog.show() }) {
                    Text(dueDateText.ifEmpty { "選擇日期" })
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (newTaskTitle.isNotBlank()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val newTask = ApiClient.api.createTask(
                                    mapOf("title" to newTaskTitle, "dueDate" to dueDateText)
                                )
                                tasks = listOf(newTask) + tasks
                                newTaskTitle = ""
                                dueDateText = ""
                            } catch (e: Exception) {
                                Log.e("TASK_CREATE", e.message ?: "")
                            }
                        }
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("新增任務")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 任務列表
        LazyColumn {
            items(tasks) { task ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = task.done,
                            onCheckedChange = { checked ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        val updated = ApiClient.api.updateTask(
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

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            if (editingTaskId == task.id) {
                                OutlinedTextField(
                                    value = editingText,
                                    onValueChange = { editingText = it },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            CoroutineScope(Dispatchers.IO).launch {
                                                try {
                                                    val updated = ApiClient.api.updateTask(
                                                         task.id,
                                                        TaskUpdateRequest(title = editingText, done = task.done)
                                                    )
                                                    tasks = tasks.map { if (it.id == task.id) updated else it }
                                                    editingTaskId = null
                                                } catch (e: Exception) {
                                                    Log.e("TASK_EDIT_FAIL", e.message ?: "")
                                                }
                                            }
                                        }
                                    )
                                )
                            } else {
                                Text(
                                    text = task.title,
                                    style = if (task.done)
                                        MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.LineThrough)
                                    else
                                        MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.clickable {
                                        editingTaskId = task.id
                                        editingText = task.title
                                    }
                                )
                            }

                            task.dueDate?.takeIf { it.isNotEmpty() }?.let {
                                Text(
                                    text = "📅 截止：${it.take(10)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                            }
                        }

                        IconButton(onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    ApiClient.api.deleteTask( task.id)
                                    tasks = tasks.filterNot { it.id == task.id }
                                } catch (e: Exception) {
                                    Log.e("TASK_DELETE", e.message ?: "")
                                }
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "刪除")
                        }

                        if (editingTaskId == task.id) {
                            IconButton(onClick = { editingTaskId = null }) {
                                Icon(Icons.Default.Close, contentDescription = "取消編輯")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun TaskListScreenPreview() {
    TaskListScreen(onLogout = {})
}