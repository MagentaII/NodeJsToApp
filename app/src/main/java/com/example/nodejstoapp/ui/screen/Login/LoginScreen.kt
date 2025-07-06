package com.example.nodejstoapp.ui.screen.Login

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nodejstoapp.network.ApiClient
import com.example.nodejstoapp.sp.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: (String, String) -> Unit,
    onGoToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(24.dp)
            .fillMaxSize()
    ) {
        Text(text = "登入帳號", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    error = "Email 與密碼不可為空"
                    return@Button
                }

                isLoading = true
                error = null
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = ApiClient.api.login(
                            mapOf("email" to email, "password" to password)
                        )
                        Log.d("LoginScreen", "accessToken: ${response.accessToken}")
                        Log.d("LoginScreen", "refreshToken: ${response.refreshToken}")
                        onLoginSuccess(response.accessToken, response.refreshToken)
                    } catch (e: Exception) {
                        error = "登入失敗：${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(if (isLoading) "登入中..." else "登入")
        }

        TextButton(
            onClick = onGoToRegister,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Text("尚未註冊？點此註冊")
        }

        error?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            )
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        onLoginSuccess = { _, _ -> },
        onGoToRegister = {}
    )
}