package com.example.nodejstoapp.ui.screen.Register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.nodejstoapp.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(onBackToLogin: () -> Unit, modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier.padding(16.dp)) {
        Text("註冊帳號", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val body = mapOf("email" to email, "password" to password)
                        ApiClient.api.register(body)
                        message = "註冊成功"
                    } catch (e: Exception) {
                        message = "註冊失敗: ${e.message}"
                    }
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("註冊")
        }

        TextButton(onClick = onBackToLogin) {
            Text("返回登入")
        }
        message?.let {
            Text(it, color = if (it.contains("成功")) Color.Green else Color.Red)
        }
    }
}