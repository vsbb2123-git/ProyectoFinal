package com.vsantamaria.proyectofinal.database.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsantamaria.proyectofinal.database.daos.CommentsDAO
import com.vsantamaria.proyectofinal.database.entities.Comments
import com.vsantamaria.proyectofinal.database.models.FullComment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CommentsViewModel(private val commentsDao: CommentsDAO) : ViewModel() {

    fun getCommentsByUser(userId: Int): List<FullComment> {
        return runBlocking(Dispatchers.IO) {
            commentsDao.getCommentsByUser(userId)
        }
    }

    fun getCommentsByGame(gameId: Int): List<FullComment> {
        return runBlocking(Dispatchers.IO) {
            commentsDao.getCommentsByGame(gameId)
        }
    }

    fun hasUserCommentedOnGame(userId: Int, gameId: Int): Boolean {
        return runBlocking(Dispatchers.IO) {
            commentsDao.hasUserCommentedOnGame(userId, gameId) > 0
        }
    }

    fun insertComment(comment: Comments): Long {
        return runBlocking(Dispatchers.IO) {
            commentsDao.insertComment(comment)
        }
    }

    fun deleteCommentById(commentId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            commentsDao.deleteCommentById(commentId)
        }
    }

    fun deleteCommentsByUserId(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            commentsDao.deleteCommentByUserId(userId)
        }
    }
}