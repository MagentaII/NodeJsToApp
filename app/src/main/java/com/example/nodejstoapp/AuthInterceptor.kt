package com.example.nodejstoapp

import android.content.Context
import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val ctx = context.applicationContext // ✅ 防止記憶體洩漏
        val originalRequest = chain.request()
        val accessToken = SessionManager.getAccessToken(ctx)
        Log.d("AuthInterceptor", "accessToken: $accessToken")
        val requestWithToken = originalRequest.newBuilder().apply {
            if (!accessToken.isNullOrEmpty()) {
                addHeader("Authorization", "Bearer $accessToken")
            }
        }.build()

        val response = chain.proceed(requestWithToken)

        return if (response.code == 403) { // 403代表Token驗證失敗
            Log.d("AuthInterceptor", "Token expired or invalid")
            response.close()

            val refreshToken = SessionManager.getRefreshToken(ctx)
            Log.d("AuthInterceptor", "refreshToken: $refreshToken")
            if (!refreshToken.isNullOrEmpty()) {
                val newToken = runBlocking {
                    try {
                        refreshAccessToken(refreshToken)
                    } catch (e: Exception) {
                        null
                    }
                }

                if (!newToken.isNullOrEmpty()) {
                    SessionManager.saveTokens(ctx, newToken, refreshToken)
                    val retryRequest = originalRequest.newBuilder()
                        .addHeader("Authorization", "Bearer $newToken")
                        .build()
                    chain.proceed(retryRequest)
                } else {
                    SessionManager.clearTokens(ctx) // ✅ 清除已過期 token
                    response
                }
            } else {
                response
            }
        } else {
            response
        }
    }

    private suspend fun refreshAccessToken(refreshToken: String): String? =
        suspendCoroutine { continuation ->
            val client = OkHttpClient()
            val body = """{"refreshToken":"$refreshToken"}"""
                .toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("https://android-notes-api.onrender.com/api/auth/refresh") // ✅ 自動刷新端點
                .post(body)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!it.isSuccessful) {
                            continuation.resume(null)
                            return
                        }

                        val responseBody = it.body?.string()
                        val json = JSONObject(responseBody ?: "{}")
                        val newAccessToken = json.optString("accessToken", null)
                        continuation.resume(newAccessToken)
                    }
                }
            })
        }

}