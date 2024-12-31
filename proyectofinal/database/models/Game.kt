package com.vsantamaria.proyectofinal.database.models

data class Game(
    val id: Int,
    val name: String,
    val released: String?,
    val background_image: String?
)