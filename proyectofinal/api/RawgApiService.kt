package com.vsantamaria.proyectofinal.api

import com.vsantamaria.proyectofinal.database.models.Game
import com.vsantamaria.proyectofinal.database.models.GamesResponse
import com.vsantamaria.proyectofinal.database.models.ScreenshotsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawgApiService {

    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int, /// la pagina de la que pilla los juegos (1ª:1-20, 2ª:21-40)
        @Query("page_size") pageSize: Int, /// numero de juegos por pagina
        @Query("search") search: String? = null,
        @Query("genres") genres: String? = null,
        @Query("tags") tags: String? = null
    ): GamesResponse


    @GET("games/{id}")
    suspend fun getGameDetails(
        @Path("id") id: String
    ): Game

    @GET("games/{id}/screenshots")
    suspend fun getGameScreenshots(
        @Path("id") id: String
    ): ScreenshotsResponse
}
