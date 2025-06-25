package com.example.nodejstoapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nodejstoapp.network.ApiClient
import kotlinx.coroutines.launch

class AppViewModel: ViewModel() {

    fun loginAndGetUsers() {
        viewModelScope.launch {
            try {
                val loginBody = mapOf("email" to "test@example.com", "password" to "1234567890")
                val response = ApiClient.api.login(loginBody)

                val token = response.token
                Log.d("TOKEN", token)

                val users = ApiClient.api.getUsers("Bearer $token")
                Log.d("USERS", users.toString())

            } catch (e: Exception) {
                Log.e("API_ERROR", e.message.toString())
            }
        }
    }

}