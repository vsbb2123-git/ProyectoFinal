package com.vsantamaria.proyectofinal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.vsantamaria.proyectofinal.api.Client
import com.vsantamaria.proyectofinal.api.RawgApiService
import com.vsantamaria.proyectofinal.database.models.Game
import com.vsantamaria.proyectofinal.database.viewmodels.UsersViewModel
import com.vsantamaria.proyectofinal.repository.GamesRepository
import com.vsantamaria.proyectofinal.ui.components.MyScaffold
import com.vsantamaria.proyectofinal.ui.factories.WishListScreenViewModelFactory
import com.vsantamaria.proyectofinal.ui.viewmodels.WishListScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun WishListScreen(
    navController: NavController,
    usersViewModel: UsersViewModel
) {
    val gamesRepository = GamesRepository(Client.retrofit.create(RawgApiService::class.java))
    val factory = WishListScreenViewModelFactory(gamesRepository) /// Se crea el Factory para el ViewModel
    val viewModel: WishListScreenViewModel = viewModel(factory = factory)
    val currentUser by usersViewModel.getCurrentUser().observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()
    val scope = rememberCoroutineScope()
    val games = remember { mutableStateListOf<Game>() }
    val wishListGames by viewModel.wishListGames.observeAsState(emptyList())


    LaunchedEffect(currentUser) {
        currentUser?.wishList?.let { viewModel.fetchWishList(it) }///solo coge la wishlist si hay un usuario y tiene una wishlist
    }

    LaunchedEffect(wishListGames) {
        games.clear()
        games.addAll(wishListGames)
    }

    MyScaffold(
        title = "Juegos favoritos",
        navController = navController,
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Tu lista de favoritos está vacía",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)

            ) {
                items(games) { game ->
                    WishListGameCard(
                        game = game,
                        onClick = {
                            navController.navigate("Game_card_screen/${game.id}")
                        },
                        onRemove = {
                            currentUser?.id?.let { userId ->
                                scope.launch {
                                    usersViewModel.removeFromWishList(userId, game.id) ///se quita el juego de la wishlist del usuario
                                    games.remove(game) ///se quita el juego manualmente de la lazycolumn
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun WishListGameCard(game: Game, onClick: () -> Unit, onRemove: () -> Unit) {
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
            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth(0.8f)){
                    Text(text = game.name, style = MaterialTheme.typography.titleLarge)
                    game.released?.let {
                        Text(
                            text = "Lanzamiento: $it",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                IconButton(
                    onClick = { onRemove() },
                    Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

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