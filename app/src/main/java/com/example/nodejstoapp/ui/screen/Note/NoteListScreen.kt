package com.example.nodejstoapp.ui.screen.Note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nodejstoapp.model.Note
import com.example.nodejstoapp.network.ApiClient

@Composable
fun NoteListScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = remember {
        NoteViewModel(NoteRepositoryImpl(ApiClient.api))
    }

    val notes by viewModel.notes.collectAsState()
    val error by viewModel.error.collectAsState()

    var newTitle by remember { mutableStateOf("") }
    var newContent by remember { mutableStateOf("") }

    var editingNoteId by remember { mutableStateOf<String?>(null) }
    var editTitle by remember { mutableStateOf("") }
    var editContent by remember { mutableStateOf("") }

    // 載入筆記
    LaunchedEffect(Unit) {
        viewModel.loadNotes()
    }

    Column(modifier = modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onLogout) {
                Text("登出")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("新增筆記", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = newTitle,
            onValueChange = { newTitle = it },
            label = { Text("標題") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = newContent,
            onValueChange = { newContent = it },
            label = { Text("內容") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
        Button(
            onClick = {
                viewModel.addNote(newTitle, newContent)
                newTitle = ""
                newContent = ""
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("新增筆記")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("筆記列表", style = MaterialTheme.typography.titleMedium)

        LazyColumn {
            items(notes) { note ->
                NoteCard(
                    note = note,
                    isEditing = (editingNoteId == note.id),
                    editTitle = editTitle,
                    editContent = editContent,
                    onEditClick = {
                        editingNoteId = note.id
                        editTitle = note.title
                        editContent = note.content
                    },
                    onCancelEdit = { editingNoteId = null },
                    onSaveEdit = { updatedTitle, updatedContent ->
                        viewModel.updateNote(note.id, updatedTitle, updatedContent)
                        editingNoteId = null
                    },
                    onDeleteClick = {
                        viewModel.deleteNote(note.id)
                    }
                )
            }
        }

        error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = Color.Red)
        }
    }
}

@Composable
fun NoteCard(
    note: Note,
    isEditing: Boolean,
    editTitle: String,
    editContent: String,
    onEditClick: () -> Unit,
    onCancelEdit: () -> Unit,
    onSaveEdit: (String, String) -> Unit,
    onDeleteClick: () -> Unit
) {
    var localTitle by remember { mutableStateOf(editTitle) }
    var localContent by remember { mutableStateOf(editContent) }

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            if (isEditing) {
                OutlinedTextField(
                    value = localTitle,
                    onValueChange = { localTitle = it },
                    label = { Text("標題") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = localContent,
                    onValueChange = { localContent = it },
                    label = { Text("內容") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Button(onClick = { onSaveEdit(localTitle, localContent) }) {
                        Text("儲存")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onCancelEdit) {
                        Text("取消")
                    }
                }
            } else {
                Text("📝 ${note.title}", style = MaterialTheme.typography.titleMedium)
                if (note.content.isNotEmpty()) {
                    Text(note.content, modifier = Modifier.padding(top = 4.dp))
                }
                Text(note.createdAt, style = MaterialTheme.typography.labelSmall)

                Row(modifier = Modifier.padding(top = 8.dp)) {
                    TextButton(onClick = onEditClick) {
                        Text("編輯")
                    }
                    TextButton(onClick = onDeleteClick) {
                        Text("刪除", color = Color.Red)
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun NoteListScreenPreview() {
    NoteListScreen(onLogout = {})
}