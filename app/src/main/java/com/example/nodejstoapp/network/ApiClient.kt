package com.example.nodejstoapp.network

import android.content.Context
import com.example.nodejstoapp.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
//    private const val BASE_URL = "http://10.0.2.2:3000/"
    private const val BASE_URL = "https://android-notes-api.onrender.com/"
    lateinit var api: ApiService
    fun init(context: Context) {
        api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context.applicationContext))
                .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    }