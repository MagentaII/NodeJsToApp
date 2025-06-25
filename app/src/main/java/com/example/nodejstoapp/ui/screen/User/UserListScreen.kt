package com.example.nodejstoapp.ui.screen.User

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nodejstoapp.model.User
import com.example.nodejstoapp.network.ApiClient

@Composable
fun UserListScreen(token: String, onLogout: () -> Unit) {
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val result = ApiClient.api.getUsers("Bearer $token")
            users = result
        } catch (e: Exception) {
            error = "取得使用者失敗: ${e.message}"
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { onLogout() }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("登出")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (error != null) {
            Text(error!!, color = Color.Red)
        } else {
            LazyColumn {
                items(users) { user ->
                    Text("👤 ${user.name}", modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun UserListScreenPreview() {
    UserListScreen(
        token = "",
        onLogout = {}
    )
}