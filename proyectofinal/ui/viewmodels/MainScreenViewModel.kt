package com.vsantamaria.proyectofinal.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsantamaria.proyectofinal.database.models.Game
import com.vsantamaria.proyectofinal.repository.GamesRepository
import kotlinx.coroutines.launch

class MainScreenViewModel(private val gamesRepository: GamesRepository) : ViewModel() {

    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>> get() = _games

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchGames() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val gamesList = gamesRepository.getGames(page = 1, pageSize = 20)
                _games.postValue(gamesList)
            } catch (e: Exception) {
                _error.postValue("Error al obtener los juegos: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

}