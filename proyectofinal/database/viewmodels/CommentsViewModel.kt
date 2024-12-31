package com.vsantamaria.proyectofinal.database.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsantamaria.proyectofinal.database.daos.CommentsDAO
import com.vsantamaria.proyectofinal.database.entities.Comments
import com.vsantamaria.proyectofinal.database.models.FullComment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CommentsViewModel(private val commentsDao: CommentsDAO) : ViewModel() {
    fun getCommentsByUser(userId: Int): Flow<List<FullComment>> {
        return commentsDao.getCommentsByUser(userId)
    }

    fun getCommentsByGame(gameId: Int): Flow<List<FullComment>> {
        return commentsDao.getCommentsByGame(gameId)
    }

    fun insertComment(comment: Comments) {
        viewModelScope.launch {
            commentsDao.insertComment(comment)
        }
    }

    fun deleteComment(commentId: Int) {
        viewModelScope.launch {
            commentsDao.deleteCommentById(commentId)
        }
    }
}