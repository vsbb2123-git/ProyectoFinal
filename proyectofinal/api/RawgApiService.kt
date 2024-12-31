package com.vsantamaria.proyectofinal.api

import com.vsantamaria.proyectofinal.database.models.GamesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RawgApiService {
    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): GamesResponse
}
