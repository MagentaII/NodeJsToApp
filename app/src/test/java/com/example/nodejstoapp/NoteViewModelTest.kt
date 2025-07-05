package com.example.nodejstoapp

import com.example.nodejstoapp.ui.screen.Note.NoteViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class NoteViewModelTest {

    private lateinit var viewModel: NoteViewModel
    private lateinit var fakeRepository: FakeNoteRepository

    @Before
    fun setup() {
        fakeRepository = FakeNoteRepository()
        viewModel = NoteViewModel(fakeRepository)
    }

    @Test
    fun `新增記事應該加入筆記列表`() = runTest {
        viewModel.addNote("Test Note", "Test Content")

        val notes = viewModel.notes.first { it.isNotEmpty() }
        assertEquals(1, notes.size)
        assertEquals("Test Note", notes[0].title)
        assertEquals("Test Content", notes[0].content)
    }

    @Test
    fun `刪除記事應該從列表移除`() = runTest {
        viewModel.addNote("Title", "Content")
        val noteId = viewModel.notes.first { it.isNotEmpty() }.first().id
        viewModel.deleteNote(noteId)

        val emptyNotes = viewModel.notes.first { it.isEmpty() }
        assertTrue(emptyNotes.isEmpty())
    }
}