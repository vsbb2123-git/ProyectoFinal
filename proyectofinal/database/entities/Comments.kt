package com.vsantamaria.proyectofinal.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "comments",
    foreignKeys = [
        ForeignKey(entity = Users::class, parentColumns = ["id"], childColumns = ["idUser"]),
        ForeignKey(entity = Games::class, parentColumns = ["id"], childColumns = ["idGame"])
    ]
)
data class Comments(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idUser: Int,
    val idGame: Int,
    val text: String,
    val date: Long = System.currentTimeMillis()
)