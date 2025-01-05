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
    ///hacer lo del pie de pagina raro ese que permite cambiar la pagina y tal
    suspend fun getGamesBySearch(search: String, page: Int, pageSize: Int): List<Game> {
        return apiService.getGames(search = search, page = page, pageSize = pageSize).results
    }

    suspend fun filterGamesByGenre(genre: String, page: Int, pageSize: Int): List<Game> {
        return apiService.getGames(genres = genre, page = page, pageSize = pageSize).results
    }

    suspend fun filterGamesByTags(tags: String, page: Int, pageSize: Int): List<Game> {
        return apiService.getGames(tags = tags, page = page, pageSize = pageSize).results
    }

    suspend fun getFilteredGames(search: String? = null, genre: String? = null, tags: String? = null, page: Int, pageSize: Int): List<Game> {
        return apiService.getGames(search = search, genres = genre, tags = tags, page = page, pageSize = pageSize).results
    }

    suspend fun getGameDetails(id: String): Game {
        val game = apiService.getGameDetails(id)
        val screenshots = apiService.getGameScreenshots(id).results
        game.screenshots = screenshots
        return game
    }

}