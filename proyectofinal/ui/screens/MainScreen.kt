package com.vsantamaria.proyectofinal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.vsantamaria.proyectofinal.api.Client
import com.vsantamaria.proyectofinal.api.RawgApiService
import com.vsantamaria.proyectofinal.database.models.Game
import com.vsantamaria.proyectofinal.database.models.Genre
import com.vsantamaria.proyectofinal.database.models.Tag
import com.vsantamaria.proyectofinal.database.viewmodels.UsersViewModel
import com.vsantamaria.proyectofinal.repository.GamesRepository
import com.vsantamaria.proyectofinal.ui.components.MyScaffold
import com.vsantamaria.proyectofinal.ui.factories.MainScreenViewModelFactory
import com.vsantamaria.proyectofinal.ui.viewmodels.MainScreenViewModel

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
    var page by remember { mutableStateOf(1) }
    var pageSize by remember { mutableStateOf(20) }
    var searchExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var genreQuery by remember { mutableStateOf("") }
    var tagsQuery by remember { mutableStateOf("") }
    var genreExpanded by remember { mutableStateOf(false) }
    var selectedGenre by remember { mutableStateOf<Genre?>(null) }
    var tagExpanded by remember { mutableStateOf(false) }
    var selectedTag by remember { mutableStateOf<Tag?>(null) }

    val genres = listOf(/// Lista con los generos
        Genre("", "Ninguno"),
        Genre("action", "Acción"),
        Genre("indie", "Indie"),
        Genre("adventure", "Aventura"),
        Genre("strategy", "Estrategia"),
        Genre("shooter", "Disparos"),
        Genre("arcade", "Arcade"),
        Genre("platformer", "Plataformas"),
        Genre("fighting", "Lucha"),
        Genre("sports", "Deportes"),
        Genre("casual", "Casual")
    )


    val tags = listOf(/// Lista con las tags
        Tag(0, "", "Ninguno"),
        Tag(1, "singleplayer", "Un jugador"),
        Tag(2, "multiplayer", "Multijugador"),
        Tag(3, "atmospheric", "Atmosférico"),
        Tag(4, "cooperative", "Cooperativo"),
        Tag(5, "difficult", "Difícil"),
        Tag(6, "survival", "Supervivencia"),
        Tag(7, "comedy", "Comedia"),
        Tag(8, "stealth", "Sigilo"),
        Tag(9, "controller", "Mando"),
        Tag(10, "zombies", "Zombis")
    )

    LaunchedEffect(currentUser) {
        userLoggedIn = currentUser != null
    }

    LaunchedEffect(Unit) {///Se cargan los juegos cuando la pantalla se crea
        viewModel.fetchGames(page, pageSize)
    }

    MyScaffold(
        title = "Lista de Juegos", /// nombre pendiente de cambio
        navController = navController, /// se pasa al scaffold si el usuario está logueado o no, cambiar por logged.value despues de las pruebasusers
        usersViewModel = usersViewModel
    ) {

        Column(modifier = Modifier.fillMaxSize()) {/// Barra de busqueda
            Button(
                onClick = { searchExpanded = !searchExpanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(if (searchExpanded) "Ocultar Búsqueda" else "Mostrar Búsqueda")
            }

            if (searchExpanded) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxHeight( 0.38f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text("Buscar por nombre") },
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        ExposedDropdownMenuBox(///Géneros
                            expanded = genreExpanded,
                            onExpandedChange = { genreExpanded = !genreExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedGenre?.nameSpanish ?: "Seleccionar",
                                onValueChange = {},
                                modifier = Modifier
                                    .fillMaxWidth(0.5f),
                                label = { Text("Género") },
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = genreExpanded)
                                }
                            )
                            ExposedDropdownMenu(
                                expanded = genreExpanded,
                                onDismissRequest = { genreExpanded = false }
                            ) {
                                genres.forEach { genre ->
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedGenre = genre
                                            genreQuery = genre.nameEnglish
                                            genreExpanded = false
                                        },
                                        text = { Text(genre.nameSpanish) }
                                    )
                                }
                            }
                        }

                        ExposedDropdownMenuBox(///etiquetas
                            expanded = tagExpanded,
                            onExpandedChange = { tagExpanded = !tagExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedTag?.nameSpanish ?: "Seleccionar",
                                onValueChange = {},
                                modifier = Modifier
                                    .fillMaxWidth(),
                                label = { Text("Etiqueta") },
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = tagExpanded)
                                }
                            )
                            ExposedDropdownMenu(
                                expanded = tagExpanded,
                                onDismissRequest = { tagExpanded = false }
                            ) {
                                tags.forEach { tag ->
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedTag = tag
                                            tagsQuery = tag.nameEnglish
                                            tagExpanded = false
                                        },
                                        text = { Text(tag.nameSpanish) }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            viewModel.filterGamesByAll(
                                search = searchQuery.ifEmpty { null },
                                genre = genreQuery.ifEmpty { null },
                                tags = tagsQuery.ifEmpty { null },
                                page = page,
                                pageSize = pageSize
                            )
                        },
                    ) {
                        Text("Buscar")
                    }
                }
            }
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
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight((if (searchExpanded) 0.80f else 0.879f))
                            .padding(6.dp)
                    ) {
                        items(games) { game ->
                            GameCard(game = game, onClick = {
                                navController.navigate("Game_card_screen/${game.id}")
                            })
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))///paginador
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.fillMaxWidth(0.05f))
                        Button(
                            onClick = {
                                if (page > 1) {
                                    page -= 1
                                    viewModel.fetchGames(page, pageSize)
                                }
                            },
                            enabled = page > 1,
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Anterior",
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.fillMaxWidth(0.04f))

                        Text(
                            text = "Pág: $page",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )

                        Spacer(modifier = Modifier.fillMaxWidth(0.04f))

                        Button(
                            onClick = {
                                page += 1
                                viewModel.fetchGames(page, pageSize)
                            },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowForward,
                                contentDescription = "Siguiente",
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.fillMaxWidth(0.1f))///antes eran 30 dp

                        Text(
                            text = "Juegos por pagina",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .width(70.dp)
                        )

                        Spacer(modifier = Modifier.fillMaxWidth(0.1f))

                        Box(
                            modifier = Modifier
                                .wrapContentSize(Alignment.TopEnd)
                        ) {
                            var expanded by remember { mutableStateOf(false) }
                            var selectedPageSize by remember { mutableStateOf(pageSize.toString()) }

                            Button(
                                onClick = { expanded = true },
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(40.dp),
                                shape = RoundedCornerShape(0.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = selectedPageSize,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                listOf(10, 20, 30, 40, 50).forEach { size ->
                                    DropdownMenuItem(
                                        onClick = {
                                            pageSize = size
                                            selectedPageSize = size.toString()
                                            expanded = false
                                            viewModel.fetchGames(page, pageSize)

                                        },
                                        text = {
                                            Text("$size")
                                        }
                                    )
                                }
                            }
                        }
                    }

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
