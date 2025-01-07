package com.vsantamaria.proyectofinal.ui.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vsantamaria.proyectofinal.repository.GamesRepository
import com.vsantamaria.proyectofinal.ui.viewmodels.WishListScreenViewModel

@Suppress("UNCHECKED_CAST")
class WishListScreenViewModelFactory(
    private val gamesRepository: GamesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WishListScreenViewModel(gamesRepository) as T
    }
}

