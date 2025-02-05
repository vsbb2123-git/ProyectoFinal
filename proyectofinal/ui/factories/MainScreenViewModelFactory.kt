package com.vsantamaria.proyectofinal.ui.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vsantamaria.proyectofinal.repository.GamesRepository
import com.vsantamaria.proyectofinal.ui.viewmodels.MainScreenViewModel

@Suppress("UNCHECKED_CAST")
class MainScreenViewModelFactory(
    private val gamesRepository: GamesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainScreenViewModel(gamesRepository) as T
    }
}

