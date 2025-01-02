package com.vsantamaria.proyectofinal.api

import com.vsantamaria.proyectofinal.database.models.Game
import com.vsantamaria.proyectofinal.database.models.GamesResponse
import com.vsantamaria.proyectofinal.database.models.ScreenshotsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawgApiService {

    @GET("games")
    suspend fun getGames(///esto es para la lista
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): GamesResponse

    @GET("games/{id}")
    suspend fun getGameDetails(///este y el de abajo son para los detalles
        @Path("id") id: String
    ): Game

    @GET("games/{id}/screenshots")
    suspend fun getGameScreenshots(
        @Path("id") id: String
    ): ScreenshotsResponse
}
