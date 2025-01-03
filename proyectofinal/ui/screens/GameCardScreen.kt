package com.vsantamaria.proyectofinal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.vsantamaria.proyectofinal.api.translateText
import com.vsantamaria.proyectofinal.database.daos.UsersDAO
import com.vsantamaria.proyectofinal.database.models.Game
import com.vsantamaria.proyectofinal.database.models.Screenshot
import com.vsantamaria.proyectofinal.database.viewmodels.UsersViewModel
import com.vsantamaria.proyectofinal.repository.GamesRepository
import com.vsantamaria.proyectofinal.ui.components.MyScaffold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                GameDetails(game = game!!)
        }
    }
}


@Composable
fun GameDetails(game: Game) {
    var showTranslation by remember { mutableStateOf(false) }
    var translatedDescription by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = game.name,
            style = MaterialTheme.typography.headlineLarge
        )
        game.background_image?.let { imageUrl ->
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
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
                .height((0.35f * LocalConfiguration.current.screenHeightDp).dp)///como necesita .dp, al multiplicar f*dp da f o algo por el estilo
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f))
                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        ) {
            Text(
                text = if (showTranslation) {
                    val cutTranslatedDescription = extractBeforeWord(translatedDescription ?: "Traduciendo...", word = "Español")
                    android.text.Html.fromHtml(
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

        Text(
            text = "Calificación: ${game.rating ?: "N/A"}",
            style = MaterialTheme.typography.bodyLarge
        )
        if (game.screenshots?.isNotEmpty() == true) {
            Text(
                text = "Capturas de pantalla",
                style = MaterialTheme.typography.headlineSmall
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(game.screenshots!!) { screenshot ->
                    ScreenshotCard(screenshot)
                }
            }
        }
    }
}

@Composable
fun ScreenshotCard(screenshot: Screenshot) {
    Image(
        painter = rememberAsyncImagePainter(screenshot.image),
        contentDescription = null,
        modifier = Modifier
            .size(150.dp)
            .padding(8.dp),
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