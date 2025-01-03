package com.vsantamaria.proyectofinal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.vsantamaria.proyectofinal.api.Client
import com.vsantamaria.proyectofinal.api.RawgApiService
import com.vsantamaria.proyectofinal.database.daos.UsersDAO
import com.vsantamaria.proyectofinal.database.models.Game
import com.vsantamaria.proyectofinal.database.viewmodels.UsersViewModel
import com.vsantamaria.proyectofinal.repository.GamesRepository
import com.vsantamaria.proyectofinal.ui.components.MyScaffold
import com.vsantamaria.proyectofinal.ui.factories.MainScreenViewModelFactory
import com.vsantamaria.proyectofinal.ui.viewmodels.MainScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, usersViewModel: UsersViewModel) {
    /// Crear el GamesRepository
    val gamesRepository = GamesRepository(Client.retrofit.create(RawgApiService::class.java))
    val scope = rememberCoroutineScope()
    var userLoggedIn by remember { mutableStateOf(false) }
    val factory = MainScreenViewModelFactory(gamesRepository) /// Se crea el Factory para el ViewModel
    val viewModel: MainScreenViewModel = viewModel(factory = factory) /// Se crea el ViewModel con el Factory
    val games by viewModel.games.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState(null)
    val currentUser by usersViewModel.getCurrentUser().observeAsState()

    LaunchedEffect(currentUser) {
        userLoggedIn = currentUser != null
    }

    LaunchedEffect(Unit) {///Se cargan los juegos cuando la pantalla se crea
        viewModel.fetchGames()
    }

    MyScaffold(
        title = "Lista de Juegos", /// nombre pendiente de cambio
        navController = navController,/// se pasa al scaffold si el usuario está logueado o no, cambiar por logged.value despues de las pruebasusers
        usersViewModel = usersViewModel
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cargando...",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        } else if (error != null) {
            Text("Error: $error")
        } else if (games.isEmpty()) {
            Text("No se encontraron juegos.")
        }
        else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(games) { game ->
                    GameCard(game = game, onClick = {
                        navController.navigate("Game_card_screen/${game.id}")
                    })
                }
            }
        }
    }
}

@Composable
fun GameCard(game: Game, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(0.85f)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = game.name, style = MaterialTheme.typography.titleLarge)
            game.released?.let {
                Text(
                    text = "Lanzamiento: $it",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            game.background_image?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}