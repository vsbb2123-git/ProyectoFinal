package com.vsantamaria.proyectofinal.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vsantamaria.proyectofinal.database.entities.Games
import kotlinx.coroutines.flow.Flow

@Dao
interface GamesDAO {
    @Insert
    fun insertGame(game: Games)

    @Query("SELECT * FROM games WHERE id = :id")
    fun getGameById(id: Int): Games

    @Query("SELECT * FROM games")
    fun getAllGames(): List<Games>

    @Query("DELETE FROM games WHERE id = :id")
    fun deleteGameById(id: Int)
}