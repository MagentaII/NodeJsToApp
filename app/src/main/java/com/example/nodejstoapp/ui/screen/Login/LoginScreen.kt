package com.example.nodejstoapp.ui.screen.Login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nodejstoapp.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    onGoToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
        ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            isError = error != null,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            isError = error != null,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val body = mapOf("email" to email, "password" to password)
                        val response = ApiClient.api.login(body)
                        onLoginSuccess(response.token)
                    } catch (e: Exception) {
                        error = "Login failed: ${e.message}"
                    }
                }
            }, modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Login")
        }

        TextButton(onClick = onGoToRegister) {
            Text("no account? register")
        }

        error?.let {
            Text(it, color = Color.Red)
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(onLoginSuccess = {}, onGoToRegister = {})
}