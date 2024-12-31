package com.vsantamaria.proyectofinal.database.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.vsantamaria.proyectofinal.database.daos.UsersDAO
import com.vsantamaria.proyectofinal.database.entities.Users
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    fun getCurrentUser(): LiveData<Users?> {
        return liveData {
            emit(usersDao.getCurrentSessionUser())
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            usersDao.deleteUserById(id)
        }
    }
}