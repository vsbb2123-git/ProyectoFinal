package com.vsantamaria.proyectofinal.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vsantamaria.proyectofinal.database.entities.Comments
import com.vsantamaria.proyectofinal.database.models.FullComment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentsDAO {
    @Insert
    fun insertComment(comment: Comments)

    @Query("""
    SELECT 
        comments.id, 
        comments.idUser, 
        comments.idGame, 
        comments.text, 
        comments.date, 
        users.username, 
        games.titulo AS gameTitle 
    FROM comments
    INNER JOIN users ON comments.idUser = users.id
    INNER JOIN games ON comments.idGame = games.id
    WHERE comments.idUser = :userId
""")
    fun getCommentsByUser(userId: Int): List<FullComment>

    @Query("""
        SELECT comments.id, 
        comments.idUser, 
        comments.idGame, 
        comments.text, 
        comments.date, 
        users.username, 
        games.titulo AS gameTitle 
        FROM comments 
        INNER JOIN users ON comments.idUser = users.id 
        INNER JOIN games ON comments.idGame = games.id
        WHERE comments.idGame = :gameId
    """)
    fun getCommentsByGame(gameId: Int): List<FullComment>

    @Query("DELETE FROM comments WHERE id = :id")
    fun deleteCommentById(id: Int)
}
