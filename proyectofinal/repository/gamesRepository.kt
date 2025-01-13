package com.vsantamaria.proyectofinal.repository

import com.vsantamaria.proyectofinal.api.RawgApiService
import com.vsantamaria.proyectofinal.database.models.Game

class GamesRepository (private val apiService: RawgApiService) {

    suspend fun getGames(page: Int, pageSize: Int): List<Game> {
        return apiService.getGames(page, pageSize).results
    }

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