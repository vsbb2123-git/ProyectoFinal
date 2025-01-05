package com.vsantamaria.proyectofinal.database.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsantamaria.proyectofinal.database.daos.GamesDAO
import com.vsantamaria.proyectofinal.database.entities.Games
import com.vsantamaria.proyectofinal.database.entities.Users
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GamesViewModel(private val gamesDao: GamesDAO) : ViewModel() {
    fun getAllGames(): List<Games> {
        return gamesDao.getAllGames()
    }
    fun insertGame(game: Games) {
        viewModelScope.launch {
            gamesDao.insertGame(game)
        }
    }

    fun deleteGame(id: Int) {
        viewModelScope.launch {
            gamesDao.deleteGameById(id)
        }
    }
}