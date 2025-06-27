package com.example.nodejstoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.nodejstoapp.network.ApiClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ApiClient.init(context = applicationContext)
        setContent {
            Scaffold() { innerPadding ->
                AppRoot(applicationContext, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}
