package com.vsantamaria.proyectofinal.database.models


data class FullComment(
    val id: Int,
    val idUser: Int,
    val idGame: Int,
    val text: String,
    val date: Long,
    val username: String,
    val gameTitle: String
)