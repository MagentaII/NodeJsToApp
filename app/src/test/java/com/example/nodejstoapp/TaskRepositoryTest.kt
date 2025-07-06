package com.example.nodejstoapp

import com.example.nodejstoapp.network.ApiService
import com.example.nodejstoapp.ui.screen.Task.TaskRepository
import com.example.nodejstoapp.ui.screen.Task.TaskRepositoryImpl
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.fail

class TaskRepositoryTest {
    private lateinit var server: MockWebServer
    private lateinit var api : ApiService
    private lateinit var repository: TaskRepository

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()

        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        api = retrofit.create(ApiService::class.java)
        repository = TaskRepositoryImpl(api)
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun `getTasks 應該正確處理 401 Unauthorized`() = runTest {
        // 模擬回傳 401
        server.enqueue(MockResponse().setResponseCode(401))

        try {
            repository.getTasks()
            fail("應該丟出例外")
        } catch (e: HttpException) {
            assertEquals(401, e.code())
        }
    }

    @Test
    fun `getTasks 應該正確處理 500 Server Error`() = runTest {
        server.enqueue(MockResponse().setResponseCode(500))

        try {
            repository.getTasks()
            fail("應該丟出例外")
        } catch (e: HttpException) {
            assertEquals(500, e.code())
        }
    }


    @Test
    fun `getTasks 應該正確處理連線超時`() = runTest {
        val response = MockResponse()
            .setBodyDelay(3, TimeUnit.SECONDS) // 模擬延遲
            .setBody("[]")

        server.enqueue(response)

        val timeoutClient = OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.SECONDS)
            .build()

        val timeoutApi = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(timeoutClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val timeoutRepo = TaskRepositoryImpl(timeoutApi)

        try {
            timeoutRepo.getTasks()
            fail("應該超時")
        } catch (e: IOException) {
            println("模擬超時成功: ${e.message}")
            assertTrue(e is IOException)
        }
    }
}