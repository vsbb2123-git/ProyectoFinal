package com.vsantamaria.proyectofinal.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {/// necesario pa poner listas con el room
    @TypeConverter
    fun fromList(list: List<Int>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toList(value: String?): List<Int> {
        val listType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }
}