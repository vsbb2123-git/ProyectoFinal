package com.vsantamaria.proyectofinal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vsantamaria.proyectofinal.database.converters.Converter
import com.vsantamaria.proyectofinal.database.daos.CommentsDAO
import com.vsantamaria.proyectofinal.database.daos.UsersDAO
import com.vsantamaria.proyectofinal.database.entities.Comments
import com.vsantamaria.proyectofinal.database.entities.Users

@Database(
    entities = [Users::class, Comments::class],
    version = 3
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDAO
    abstract fun commentsDao(): CommentsDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()///lo de fallbackToDestructiveMigration() es para que si se quiere cambiar de "version" de la base de datos, borre la database y no de errores
                INSTANCE = instance
                instance
            }
        }
    }
}
