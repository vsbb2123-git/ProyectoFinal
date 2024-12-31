package com.vsantamaria.proyectofinal.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class Users(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val username: String,
    val password: String,
    val userType: String, /// "base", "crítico", "desarrollador", "administrador"
    val currentSession: Boolean = true
)