package com.example.nodejstoapp.ui.screen.Note

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nodejstoapp.model.Note
import com.example.nodejstoapp.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

@Composable
fun NoteListScreen(token: String, onLogout: () -> Unit, modifier: Modifier = Modifier) {
    var notes by remember { mutableStateOf<List<Note>>(emptyList()) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    // 載入筆記
    LaunchedEffect(Unit) {
        try {
            notes = ApiClient.api.getNotes("Bearer $token")
        } catch (e: Exception) {
            error = "載入筆記失敗: ${e.message}"
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        Row {
            Button(onClick = onLogout) {
                Text("登出")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("標題") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("內容") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )

        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val newNote = ApiClient.api.createNote(
                            "Bearer $token",
                            mapOf("title" to title, "content" to content)
                        )
                        Log.d("NoteListScreen", "New note created, Title: ${newNote.title}")
                        Log.d("NoteListScreen", "New note created, Content: ${newNote.content}")
                        notes = listOf(newNote) + notes
                        title = ""
                        content = ""
                    } catch (e: Exception) {
                        error = "新增失敗: ${e.message}"
                    }
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("新增筆記")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(notes) { note ->
                Log.d("NoteListScreen", "Note ID: ${note.id}")
                Log.d("NoteListScreen", "Note Title: ${note.title}")
                Log.d("NoteListScreen", "Note Content: ${note.content}")
                Log.d("NoteListScreen", "Note Created At: ${note.createdAt}")
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("📝 ${note.title}", style = MaterialTheme.typography.titleMedium)
                        if (note.content.isNotEmpty()) {
                            Text(note.content)
                        }
                        Text(note.createdAt, style = MaterialTheme.typography.labelSmall)

                        TextButton(onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    ApiClient.api.deleteNote("Bearer $token", note.id)
                                    notes = notes.filterNot { it.id == note.id }
                                } catch (e: Exception) {
                                    error = "刪除失敗: ${e.message}"
                                }
                            }
                        }) {
                            Text("刪除", color = Color.Red)
                        }
                    }
                }
            }
        }

        error?.let {
            Text(it, color = Color.Red)
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun NoteListScreenPreview() {
    NoteListScreen(token = "", onLogout = {})
}