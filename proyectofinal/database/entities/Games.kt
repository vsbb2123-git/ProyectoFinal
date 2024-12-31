package com.vsantamaria.proyectofinal.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Games(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val desarrollador: String
)