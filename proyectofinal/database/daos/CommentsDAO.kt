package com.vsantamaria.proyectofinal.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vsantamaria.proyectofinal.database.entities.Comments
import com.vsantamaria.proyectofinal.database.models.FullComment

@Dao
interface CommentsDAO {
    @Insert
    fun insertComment(comment: Comments): Long

    @Query("""
        SELECT comments.id, 
               comments.idUser, 
               comments.idGame, 
               comments.text, 
               comments.date, 
               users.username
        FROM comments
        INNER JOIN users ON comments.idUser = users.id
        WHERE comments.idUser = :userId
    """)
    fun getCommentsByUser(userId: Int): List<FullComment>

    @Query("""
        SELECT comments.id, 
               comments.idUser, 
               comments.idGame, 
               comments.text, 
               comments.date, 
               users.username
        FROM comments
        INNER JOIN users ON comments.idUser = users.id
        WHERE comments.idGame = :gameId
    """)
    fun getCommentsByGame(gameId: Int): List<FullComment>

    @Query("""
        SELECT COUNT(*) 
        FROM comments 
        WHERE idUser = :userId AND idGame = :gameId
    """)
    fun hasUserCommentedOnGame(userId: Int, gameId: Int): Int

    @Query("DELETE FROM comments WHERE id = :id")
    fun deleteCommentById(id: Int)

    @Query("DELETE FROM comments WHERE idUser = :userId")
    fun deleteCommentByUserId(userId: Int)
}
