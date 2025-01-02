package com.vsantamaria.proyectofinal.repository

import android.os.Parcel
import android.os.Parcelable
import com.vsantamaria.proyectofinal.api.Client
import com.vsantamaria.proyectofinal.api.RawgApiService
import com.vsantamaria.proyectofinal.database.models.Game

class GamesRepository (private val apiService: RawgApiService) {

    suspend fun getGames(page: Int, pageSize: Int): List<Game> {
        return apiService.getGames(page, pageSize).results
    }
    suspend fun getGameDetails(id: String): Game {
        val game = apiService.getGameDetails(id)
        val screenshots = apiService.getGameScreenshots(id).results
        game.screenshots = screenshots
        return game
    }

}