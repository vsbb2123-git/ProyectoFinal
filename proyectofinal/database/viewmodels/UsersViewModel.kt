package com.vsantamaria.proyectofinal.database.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.vsantamaria.proyectofinal.database.daos.UsersDAO
import com.vsantamaria.proyectofinal.database.entities.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UsersViewModel(private val usersDao: UsersDAO) : ViewModel(){
    fun getAllUsers(): List<Users> {
        return usersDao.getAllUsers()
    }

    fun insertUser(user: Users) {
        viewModelScope.launch {
            usersDao.insertUser(user)
        }
    }

    fun login(userId: Int) = viewModelScope.launch {
        usersDao.loginUser(userId)
    }

    fun logout(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            usersDao.logoutUser(userId)
        }
    }

    fun getCurrentUser(): LiveData<Users?> {
        return liveData(Dispatchers.IO) {
            emit(usersDao.getCurrentSessionUser())
        }
    }

    fun addToWishList(userId: Int, gameId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = usersDao.getUserById(userId)
            user?.let {
                val updatedWishList = it.wishList.toMutableList().apply { add(gameId) }
                usersDao.updateWishList(userId, updatedWishList)
            }
        }
    }

    fun removeFromWishList(userId: Int, gameId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = usersDao.getUserById(userId)
            user?.let {
                val updatedWishList = it.wishList.toMutableList().apply { remove(gameId) }
                usersDao.updateWishList(userId, updatedWishList)
            }
        }
    }

    fun logIn(username: String, password: String): String {
        return runBlocking(Dispatchers.IO) {
            val user = usersDao.getAllUsers().find { it.username == username && it.password == password }
            if (user != null) {
                usersDao.loginUser(user.id)
                ""
            } else {
                "Credenciales incorrectas"
            }
        }
    }

    fun signIn(
        username: String,
        password: String,
        confirmPassword: String,
        userType: String
    ): String {
        return runBlocking(Dispatchers.IO) {
            val passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}$".toRegex()
            if (!passwordRegex.matches(password)) {
                return@runBlocking "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número"
            }

            if (password != confirmPassword) {
                return@runBlocking "Las contraseñas no coinciden"
            }

            val existingUser = usersDao.getAllUsers().find { it.username == username }
            if (existingUser != null) {
                return@runBlocking "El nombre de usuario ya está en uso"
            }

            if (userType.isEmpty()) {
                return@runBlocking "El tipo de usuario es obligatorio"
            }

            val newUser = Users(0, username, password, userType, true)///currenstession siempre tiene que ser true de primeras
            usersDao.insertUser(newUser)
            return@runBlocking ""
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            usersDao.deleteUserById(id)
        }
    }
}
