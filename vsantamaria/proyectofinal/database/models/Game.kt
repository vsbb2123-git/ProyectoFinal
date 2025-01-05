package com.vsantamaria.proyectofinal.database.models

data class Game(
    val id: Int,
    val name: String,
    val released: String?,
    val background_image: String?,/// formato url
    val description: String?,
    val genres: List<Genre>?,
    val rating: Float?,
    val developers: List<Developer>?,
    val tags: List<Tag>?,
    var screenshots: List<Screenshot>? = null
)
