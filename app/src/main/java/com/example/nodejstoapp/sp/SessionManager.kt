package com.example.nodejstoapp.sp

import android.content.Context
import androidx.core.content.edit

object SessionManager {
    private const val PREF_NAME = "app_prefs"
    private const val KEY_ACCESS_TOKEN = "accessToken"
    private const val KEY_REFRESH_TOKEN = "refreshToken"

    fun saveTokens(context: Context, accessToken: String, refreshToken: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .apply()
    }

    fun getAccessToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    fun getRefreshToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_REFRESH_TOKEN, null)
    }

    fun clearTokens(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit { clear() }
    }

}