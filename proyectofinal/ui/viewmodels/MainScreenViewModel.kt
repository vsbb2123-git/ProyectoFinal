package com.vsantamaria.proyectofinal.ui.viewmodels

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

    fun fetchGames(page: Int, pageSize: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val gamesList = gamesRepository.getGames(page, pageSize)
                _games.postValue(gamesList)
            } catch (e: Exception) {
                _error.postValue("Error al obtener los juegos: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun filterGamesByAll(search: String? = null, genre: String? = null, tags: String? = null, page: Int, pageSize: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val filteredGames = gamesRepository.getFilteredGames(search, genre, tags, page, pageSize)
                _games.postValue(filteredGames)
            } catch (e: Exception) {
                _error.postValue("Error al aplicar filtros: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun searchGames(search: String, page: Int, pageSize: Int) {///metodos no utilizados
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val filteredGames = gamesRepository.getGamesBySearch(search, page, pageSize)
                _games.postValue(filteredGames)
            } catch (e: Exception) {
                _error.postValue("Error al buscar juegos: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun filterGamesByGenre(genre: String, page: Int, pageSize: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val filteredGames = gamesRepository.filterGamesByGenre(genre, page, pageSize)
                _games.postValue(filteredGames)
            } catch (e: Exception) {
                _error.postValue("Error al filtrar juegos por género: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun filterGamesByTags(tags: String, page: Int, pageSize: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val filteredGames = gamesRepository.filterGamesByTags(tags, page, pageSize)
                _games.postValue(filteredGames)
            } catch (e: Exception) {
                _error.postValue("Error al filtrar juegos por etiquetas: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }


}