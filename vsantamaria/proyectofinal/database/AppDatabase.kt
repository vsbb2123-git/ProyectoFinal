package com.vsantamaria.proyectofinal.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.vsantamaria.proyectofinal.database.daos.CommentsDAO
import com.vsantamaria.proyectofinal.database.daos.GamesDAO
import com.vsantamaria.proyectofinal.database.daos.UsersDAO
import com.vsantamaria.proyectofinal.database.entities.Comments
import com.vsantamaria.proyectofinal.database.entities.Games
import com.vsantamaria.proyectofinal.database.entities.Users

@Database(
    entities = [Users::class, Games::class, Comments::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDAO
    abstract fun gamesDao(): GamesDAO
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
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
