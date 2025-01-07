package com.vsantamaria.proyectofinal.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsantamaria.proyectofinal.database.models.Game
import com.vsantamaria.proyectofinal.repository.GamesRepository
import kotlinx.coroutines.launch

class WishListScreenViewModel(private val gamesRepository: GamesRepository) : ViewModel() {

    private val _wishListGames = MutableLiveData<List<Game>>()
    val wishListGames: LiveData<List<Game>> get() = _wishListGames

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchWishList(gameIdList: List<Int>) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val games = gameIdList.mapNotNull { gameId ->
                    try {
                        gamesRepository.getGameDetails(gameId.toString())
                    } catch (e: Exception) {
                        null
                    }
                }
                _wishListGames.postValue(games)
            } catch (e: Exception) {
                _error.postValue("Error al cargar la lista de deseos: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}