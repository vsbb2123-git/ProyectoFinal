package com.vsantamaria.proyectofinal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.vsantamaria.proyectofinal.api.translateText
import com.vsantamaria.proyectofinal.database.entities.Users
import com.vsantamaria.proyectofinal.database.models.Game
import com.vsantamaria.proyectofinal.database.models.Screenshot
import com.vsantamaria.proyectofinal.database.viewmodels.UsersViewModel
import com.vsantamaria.proyectofinal.repository.GamesRepository
import com.vsantamaria.proyectofinal.ui.components.MyScaffold
import com.vsantamaria.proyectofinal.ui.components.PopUpLogin
import kotlinx.coroutines.launch

@Composable
fun GameCardScreen(navController: NavController, gamesRepository: GamesRepository, usersViewModel: UsersViewModel, gameId: String?) {
    val scope = rememberCoroutineScope()
    var game by remember { mutableStateOf<Game?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val currentUser by usersViewModel.getCurrentUser().observeAsState()
    var userLoggedIn by remember { mutableStateOf(false) }

    LaunchedEffect(currentUser) {
        userLoggedIn = currentUser != null
    }

    LaunchedEffect(gameId) {
        if (gameId != null) {
            scope.launch {
                try {
                    isLoading = true
                    game = gamesRepository.getGameDetails(gameId)

                } catch (e: Exception) {
                    error = "Error: ${e.localizedMessage}"
                } finally {
                    isLoading = false
                }
            }
        } else {
            error = "No se proporcionó un ID de juego válido"
            isLoading = false
        }
    }

    MyScaffold(
        title = game?.name ?: "Cargando juego...",
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
        }else {
                GameDetails(game = game!!, currentUser = currentUser, usersViewModel = usersViewModel, navController = navController)
        }
    }
}


@Composable
fun GameDetails(game: Game, currentUser: Users?, usersViewModel: UsersViewModel, navController: NavController) {
    var showTranslation by remember { mutableStateOf(false) }
    var translatedDescription by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf<Screenshot?>(null) }
    val scope = rememberCoroutineScope()
    var translatedGenres by remember { mutableStateOf<String?>(null) }
    var translatedTags by remember { mutableStateOf<String?>(null) }
    val isInWishList = remember { mutableStateOf(currentUser?.wishList?.contains(game.id) ?: false) }
    var showPopUp by remember { mutableStateOf(false) }


    LaunchedEffect(game.genres) { /// Géneros traducidos con la API de Google
        if (!game.genres.isNullOrEmpty()) {
            scope.launch {
                translatedGenres = translateText(game.genres.joinToString(", ") { it.name })
            }
        }
    }

    LaunchedEffect(game.tags) { /// Tags traducidas con la API de Google
        if (!game.tags.isNullOrEmpty()) {
            scope.launch {
                translatedTags = translateText(game.tags.joinToString(", ") { it.name })
            }
        }
    }
    if (showPopUp) {
        PopUpLogin(
            navController = navController,
            onDismiss = { showPopUp = false }
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            game.background_image?.let { imageUrl ->///imagen de fondo
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            if (game.screenshots?.isNotEmpty() == true) {///capturas
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(game.screenshots!!) { screenshot ->
                        ScreenshotCard(
                            screenshot = screenshot,
                            onClick = {///si la captura pulsada no esta ampliada, el onclick la amplia, si no, la amplia
                                expanded = if (expanded == screenshot) null else screenshot
                            }
                        )
                    }
                }
            }

            Row(///descripcion
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Descripción:",
                    style = MaterialTheme.typography.headlineSmall
                )
                Button(onClick = {
                    if (!showTranslation && translatedDescription == null) {
                        scope.launch {
                            translatedDescription = translateText(
                                game.description ?: "Sin descripción disponible"
                            )
                        }
                    }
                    showTranslation = !showTranslation
                }) {
                    Text(
                        text = if (showTranslation) "Ver original" else "Traducir al español"
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((0.35f * LocalConfiguration.current.screenHeightDp).dp)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f))
                    .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            ) {
                Text(
                    text = if (showTranslation) {
                        val cutTranslatedDescription = extractBeforeWord(translatedDescription ?: "Traduciendo...", word = "Español")
                        android.text.Html.fromHtml(///para que no se vean cosas tipo <p> por que la api trae un texto en formato html
                            cutTranslatedDescription,
                            android.text.Html.FROM_HTML_MODE_COMPACT
                        ).toString()
                    } else {
                        val cutDescription = extractBeforeWord(game.description ?: "Sin descripción disponible", word = "Español")
                        android.text.Html.fromHtml(
                            cutDescription,
                            android.text.Html.FROM_HTML_MODE_COMPACT
                        ).toString()
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Calificación: ${game.rating ?: "N/A"}/5",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (!game.genres.isNullOrEmpty()) {
                Text(
                    text = "Géneros: ${translatedGenres ?: ""}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (!game.tags.isNullOrEmpty()) {
                Text(
                    text = "Etiquetas: ${translatedTags ?: ""}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Button(///boton lista de deseados
                onClick = {
                    if (currentUser != null) {
                        scope.launch {
                            if (isInWishList.value) {
                                usersViewModel.removeFromWishList(currentUser.id, game.id)
                            } else {
                                usersViewModel.addToWishList(currentUser.id, game.id)
                            }
                            isInWishList.value =
                                !isInWishList.value  ///no se puede poner !isInWishList.value
                        }
                    }else{
                        showPopUp = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isInWishList.value) "Quitar de la lista de deseados" else "Añadir a la lista de deseados")
            }

        }

        if (expanded != null) { ///para cuando se amplíe una captura
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                    .clickable { expanded = null },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(expanded!!.image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(16 / 9f)
                )
            }
        }
    }
}

@Composable
fun ScreenshotCard(screenshot: Screenshot, onClick: () -> Unit) {
    Image(
        painter = rememberAsyncImagePainter(screenshot.image),
        contentDescription = null,
        modifier = Modifier
            .size(150.dp)
            .clickable { onClick() },
        contentScale = ContentScale.Crop
    )
}

fun extractBeforeWord(text: String, word: String): String { ///para los casos en los que en la descripcion está el texto en ingles y despues en español, por que se separa por la palabra Español
    return if (text.contains(word)) {
        text.substringBefore(word).trim()
    } else {
        text
    }
}