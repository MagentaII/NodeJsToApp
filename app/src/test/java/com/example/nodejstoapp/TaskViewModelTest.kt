package com.example.nodejstoapp

import com.example.nodejstoapp.ui.screen.Task.TaskViewModel
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.collections.first
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class TaskViewModelTest {
    private lateinit var viewModel: TaskViewModel
    private lateinit var fakeRepository : FakeTaskRepository

    @Before
    fun setUp() {
        fakeRepository = FakeTaskRepository()
        viewModel = TaskViewModel(fakeRepository)
    }

    @Test
    fun `新增任務應該加入列表`() = runTest {
        viewModel.createTask("Test Task", "2025-07-01")

        val tasks = viewModel.tasks.first { it.isNotEmpty() }
        assertEquals("Test Task", tasks[0].title)
        assertEquals("2025-07-01", tasks[0].dueDate)
        assertFalse(tasks[0].done)
    }

    @Test
    fun `刪除任務應該移出列表`() = runTest {
        viewModel.createTask("Task to Delete", null)
        val taskId = viewModel.tasks.first { it.isNotEmpty() }.first().id
        viewModel.deleteTask(taskId)

        val emptyNotes = viewModel.tasks.first { it.isEmpty() }
        assertTrue(emptyNotes.isEmpty())
    }
}