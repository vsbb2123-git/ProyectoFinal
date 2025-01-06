package com.vsantamaria.proyectofinal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vsantamaria.proyectofinal.database.daos.CommentsDAO
import com.vsantamaria.proyectofinal.database.entities.Comments
import com.vsantamaria.proyectofinal.database.models.FullComment
import com.vsantamaria.proyectofinal.database.viewmodels.CommentsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import junit.framework.TestCase.assertEquals

class CommentsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockCommentsDao: CommentsDAO
    private lateinit var commentsViewModel: CommentsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockCommentsDao = mock()
        commentsViewModel = CommentsViewModel(mockCommentsDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `insertComment should call DAO to insert a comment`() {
        val comment = Comments(1, 1, 1, "Test comment")

        commentsViewModel.insertComment(comment)

        testDispatcher.scheduler.advanceUntilIdle()

        verify(mockCommentsDao).insertComment(comment)
    }

    @Test
    fun `getCommentsByUser should return the correct list of comments`() {
        val userId = 1
        val mockComments = listOf(
            FullComment(1, 1, 1, "Comment 1", 1632998420000, "User1", "Game1"),
            FullComment(2, 1, 2, "Comment 2", 1632998430000, "User1", "Game2")
        )

        whenever(mockCommentsDao.getCommentsByUser(userId)).thenReturn(mockComments)

        val result = commentsViewModel.getCommentsByUser(userId)

        assertEquals(mockComments, result)
    }

    @Test
    fun `getCommentsByGame should return the correct list of comments`() {
        val gameId = 1
        val mockComments = listOf(
            FullComment(1, 1, 1, "Comment 1", 1632998420000, "User1", "Game1"),
            FullComment(2, 2, 1, "Comment 2", 1632998430000, "User2", "Game1")
        )

        whenever(mockCommentsDao.getCommentsByGame(gameId)).thenReturn(mockComments)

        val result = commentsViewModel.getCommentsByGame(gameId)

        assertEquals(mockComments, result)
    }

    @Test
    fun `deleteComment should call DAO to delete a comment by ID`() {
        val commentId = 1

        commentsViewModel.deleteComment(commentId)

        testDispatcher.scheduler.advanceUntilIdle()

        verify(mockCommentsDao).deleteCommentById(commentId)
    }
}
