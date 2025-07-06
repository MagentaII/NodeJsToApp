package com.example.nodejstoapp.ui.screen.dashboard

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(context: Context) {
    val viewModel = remember { DashboardViewModel(context) }

    Column(Modifier.padding(24.dp)) {
        Text("App 總覽", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        Text("👤 使用者：${viewModel.userEmail}")
        Text("📝 筆記數量：${viewModel.noteCount} 筆")
        Text("✅ 未完成任務：${viewModel.pendingTaskCount} 項")

        Spacer(modifier = Modifier.height(32.dp))
        Text("🚧 記帳模組：尚未啟用", color = Color.Gray)
        Text("📚 單字模組：尚未啟用", color = Color.Gray)
    }
}