package com.vsantamaria.proyectofinal.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vsantamaria.proyectofinal.database.entities.Users

@Dao
interface UsersDAO {
    @Insert
    fun insertUser(user: Users)

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Int): Users?

    @Query("SELECT * FROM users")
    fun getAllUsers(): List<Users>

    @Query("SELECT * FROM users WHERE currentSession = 1 LIMIT 1")
    fun getCurrentSessionUser(): Users?

    @Query("UPDATE users SET currentSession = 1 WHERE id = :userId")
    fun loginUser(userId: Int)

    @Query("UPDATE users SET currentSession = 0 WHERE id = :userId")
    fun logoutUser(userId: Int)

    @Query("UPDATE users SET wishList = :wishList WHERE id = :userId")
    fun updateWishList(userId: Int, wishList: List<Int>)

    @Query("DELETE FROM users WHERE id = :id")
    fun deleteUserById(id: Int)
}