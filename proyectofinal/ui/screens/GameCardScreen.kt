package com.vsantamaria.proyectofinal.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.vsantamaria.proyectofinal.R
import com.vsantamaria.proyectofinal.api.translateText
import com.vsantamaria.proyectofinal.database.entities.Comments
import com.vsantamaria.proyectofinal.database.entities.Users
import com.vsantamaria.proyectofinal.database.models.FullComment
import com.vsantamaria.proyectofinal.database.models.Game
import com.vsantamaria.proyectofinal.database.models.Screenshot
import com.vsantamaria.proyectofinal.database.viewmodels.CommentsViewModel
import com.vsantamaria.proyectofinal.database.viewmodels.UsersViewModel
import com.vsantamaria.proyectofinal.repository.GamesRepository
import com.vsantamaria.proyectofinal.ui.components.CommentCard
import com.vsantamaria.proyectofinal.ui.components.MyScaffold
import com.vsantamaria.proyectofinal.ui.components.PopUpLogin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun GameCardScreen(navController: NavController, gamesRepository: GamesRepository, usersViewModel: UsersViewModel, commentsViewModel: CommentsViewModel, gameId: String?) {
    val scope = rememberCoroutineScope()
    var game by remember { mutableStateOf<Game?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val currentUser by usersViewModel.getCurrentUser().observeAsState()
    val context = LocalContext.current
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
            error = context.getString(R.string.no_se_proporcion_un_id_de_juego_v_lido)
            isLoading = false
        }
    }

    MyScaffold(
        title = game?.name ?: stringResource(R.string.cargando_juego),
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
                    text = stringResource(R.string.cargando),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        } else if (error != null) {
            Text("Error: $error")
        }else {
                GameDetails(game!!, currentUser,  usersViewModel,  commentsViewModel,  navController)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun GameDetails(game: Game, currentUser: Users?, usersViewModel: UsersViewModel, commentsViewModel: CommentsViewModel, navController: NavController) {
    var showTranslation by remember { mutableStateOf(false) }
    var translatedDescription by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf<Screenshot?>(null) }
    val scope = rememberCoroutineScope()
    var translatedGenres by remember { mutableStateOf<String?>(null) }
    var translatedTags by remember { mutableStateOf<String?>(null) }
    val isInWishList = remember { mutableStateOf(currentUser?.wishList?.contains(game.id) ?: false) }
    var showPopUp by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }
    val comments = remember { mutableStateOf(emptyList<FullComment>()) }
    var hasCommented by remember { mutableStateOf(false) }
    var isThisGameDeveloper by remember { mutableStateOf(false) }
    var isDeveloper by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (currentUser != null) {
        isDeveloper = currentUser.userType == "Desarrollador"
        isThisGameDeveloper = game.developers?.any { it.name == currentUser.username } == true

        LaunchedEffect(currentUser.id, game.id) {
            hasCommented = commentsViewModel.hasUserCommentedOnGame(currentUser.id, game.id)
        }
    }

    LaunchedEffect(game.genres) { /// Géneros traducidos con la API de Google separados por ", "
        if (!game.genres.isNullOrEmpty()) {
            scope.launch {
                translatedGenres = translateText(game.genres.joinToString(", ") { it.name })
            }
        }
    }

    LaunchedEffect(game.tags) { /// Tags traducidas con la API de Google separadas por ", "
        if (!game.tags.isNullOrEmpty()) {
            scope.launch {
                translatedTags = translateText(game.tags.joinToString(", ") { it.name })
            }
        }
    }

    LaunchedEffect(game.id) {
        scope.launch {
            comments.value = commentsViewModel.getCommentsByGame(game.id)
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
                    text = stringResource(R.string.descripci_n),
                    style = MaterialTheme.typography.headlineSmall
                )
                Button(onClick = {
                    if (!showTranslation && translatedDescription == null) {
                        scope.launch {
                            translatedDescription = translateText(
                                game.description ?: context.getString(R.string.sin_descripci_n_disponible)
                            )
                        }
                    }
                    showTranslation = !showTranslation
                }) {
                    Text(
                        text = if (showTranslation) stringResource(R.string.ver_original) else stringResource(
                            R.string.traducir_al_espa_ol
                        )
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
                        val cutTranslatedDescription = extractBeforeWord(translatedDescription ?: stringResource(
                            R.string.traduciendo
                        ), word = "Español")
                        android.text.Html.fromHtml(///para que no se vean cosas tipo <p> por que la api trae un texto en formato html
                            cutTranslatedDescription,
                            android.text.Html.FROM_HTML_MODE_COMPACT
                        ).toString()
                    } else {
                        val cutDescription = extractBeforeWord(game.description ?: context.getString(R.string.sin_descripci_n_disponible), word = "Español")
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
                text = stringResource(
                    R.string.calificaci_n_5, game.rating ?: stringResource(R.string.n_a)
                ),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (!game.developers.isNullOrEmpty()) {
                Text(
                    text = stringResource(
                        R.string.desarrolladores,
                        game.developers.joinToString(", ") { it.name }),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (!game.genres.isNullOrEmpty()) {
                Text(
                    text = stringResource(R.string.g_neros, translatedGenres ?: ""),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (!game.tags.isNullOrEmpty()) {
                Text(
                    text = stringResource(R.string.etiquetas, translatedTags ?: ""),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(modifier = Modifier.fillMaxWidth()){
                Button(///boton lista de favoritos
                    onClick = {
                        if (currentUser != null) {
                            scope.launch {
                                if (isInWishList.value) {
                                    usersViewModel.removeFromWishList(currentUser.id, game.id)
                                } else {
                                    usersViewModel.addToWishList(currentUser.id, game.id)
                                }
                                isInWishList.value = !isInWishList.value  ///no se puede poner !isInWishList.value
                            }
                        }else{
                            showPopUp = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight()
                ) {
                    Text(if (isInWishList.value) stringResource(R.string.quitar_de_favoritos) else stringResource(
                        R.string.a_adir_a_favoritos
                    )
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        val query = Uri.encode(game.name)
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.allkeyshop.com/blog/products/?search_name=$query"))

                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(stringResource(R.string.pagina_de_compra))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (currentUser == null) {///crear/guardar comentarios
                Text(stringResource(R.string.inicia_sesi_n_para_comentar), style = MaterialTheme.typography.bodyLarge)
            } else if(hasCommented){
                Text(stringResource(R.string.solo_se_puede_scribir_un_comentario_por_usuario), style = MaterialTheme.typography.bodyLarge)
            } else if(isDeveloper && !isThisGameDeveloper ){
                Text(stringResource(R.string.no_puedes_comentar_en_este_juego_porque_no_eres_uno_de_sus_desarrolladores), style = MaterialTheme.typography.bodyLarge)
            }else {
                TextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    label = { Text(stringResource(R.string.a_ade_un_comentario)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            scope.launch {
                                val newComment = Comments(
                                    idUser = currentUser.id,
                                    idGame = game.id,
                                    text = commentText
                                )
                                val insertedId = commentsViewModel.insertComment(newComment)

                                commentText = ""

                                val commentToInsert = FullComment(///comentario que se va a insertar en la lista comments hasta que se actualize la pantalla
                                    id = insertedId.toInt(),
                                    idUser = newComment.idUser,
                                    idGame = newComment.idGame,
                                    text = newComment.text,
                                    date = System.currentTimeMillis(),
                                    username = currentUser.username
                                )
                                comments.value += commentToInsert

                            }
                            hasCommented=true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.guardar_comentario))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (comments.value.isEmpty()) {///mostrar comentarios
                Text(
                    text = stringResource(R.string.no_hay_comentarios_a_n),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                comments.value.forEach { comment ->
                    val commentUser = usersViewModel.getUserById(comment.idUser) ///el usuario del comentario
                    if (commentUser != null) {
                        CommentCard(
                            comment = comment,
                            user = commentUser,
                            currentUser = currentUser,
                            onRemove = { commentToDelete ->
                                scope.launch {
                                    commentsViewModel.deleteCommentById(commentToDelete.id) ///se borra de la base de datos
                                    comments.value = comments.value.filter { it.id != commentToDelete.id }///se borra de la lista
                                    hasCommented=false
                                }
                            }
                        )
                    }
                }
            }
            Button(
                onClick = {
                    exportGameDetailsToPDF(
                        context = context,
                        game = game,
                        commentsViewModel = commentsViewModel,
                        usersViewModel = usersViewModel,
                        translatedGenres = translatedGenres,
                        translatedTags = translatedTags
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.exportar_detalles_del_juego_a_pdf))
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

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun GameDetailsForPDF( ///version de la pantalla sin botones, con el título del juego y con la descripcion siempre en español
    game: Game,
    commentsViewModel: CommentsViewModel,
    usersViewModel:UsersViewModel,
    translatedGenres:String?,
    translatedTags:String?
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val comments = remember { mutableStateOf(emptyList<FullComment>()) }

    val translatedDescription = runBlocking {
        extractBeforeWord(translateText(game.description ?: context.getString(R.string.sin_descripci_n_disponible)), word = "Español")
    }


    LaunchedEffect(game.id) {
        scope.launch {
            comments.value = commentsViewModel.getCommentsByGame(game.id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = game.name,
            style = MaterialTheme.typography.headlineMedium,
        )

//        game.background_image?.let { imageUrl ->///para convertir las imagenes en bitmaps sin hardware
//            var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
//
//            LaunchedEffect(imageUrl) {
//                val loader = ImageLoader(context)
//                val request = ImageRequest.Builder(context)
//                    .data(imageUrl)
//                    .allowHardware(false)
//                    .build()
//                val result = loader.execute(request).drawable
//                val bitmap = (result as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, false)
//                imageBitmap = bitmap.asImageBitmap()
//            }
//
//            imageBitmap?.let { bitmap ->
//                Image(
//                    bitmap = bitmap,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp),
//                    contentScale = ContentScale.Crop
//                )
//            }
//        } NO FUNCIONA

        Text(
            text = context.getString(R.string.descripci_n),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = android.text.Html.fromHtml(
                translatedDescription,
                android.text.Html.FROM_HTML_MODE_COMPACT
            ).toString(),
            style = MaterialTheme.typography.bodyLarge,
        )


        Text(
            text = stringResource(
                R.string.calificaci_n_5, game.rating ?: stringResource(R.string.n_a)
            ),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = stringResource(R.string.g_neros, translatedGenres ?: ""),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = stringResource(R.string.etiquetas, translatedTags ?: ""),
            style = MaterialTheme.typography.bodyLarge
        )


        if (comments.value.isEmpty()) {
            Text(
                text = stringResource(R.string.no_hay_comentarios_a_n),
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            Text(
                text = stringResource(R.string.comentarios),
                style = MaterialTheme.typography.headlineMedium
            )

            comments.value.forEach { comment ->
                val commentUser = usersViewModel.getUserById(comment.idUser)
                if (commentUser != null) {
                    CommentCard(
                        comment = comment,
                        user = commentUser,
                        currentUser = null,
                        onRemove = {}
                    )
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun exportGameDetailsToPDF(
    context: Context,
    game: Game,
    commentsViewModel: CommentsViewModel,
    usersViewModel: UsersViewModel,
    translatedGenres: String?,
    translatedTags:String?
) {
    val pdfDocument = PdfDocument()
    val pageWidth = 1080
    val pageHeight = 1920

    val composeView = ComposeView(context).apply { /// se crea un ComposeView temporal (que es lo que se va exportar a pdf)
        setContent {
            MaterialTheme {
                GameDetailsForPDF(
                    game = game,
                    commentsViewModel = commentsViewModel,
                    usersViewModel = usersViewModel,
                    translatedGenres = translatedGenres,
                    translatedTags = translatedTags
                )
            }
        }
    }

    val window = (context as? Activity)?.window /// intenta convertir el context en una activity y conseguir su window
    val decorView = window?.decorView as? ViewGroup ?: return /// intenta coger todos los elementos visibles de la pantalla, si no lo consigue se para
    decorView.addView(composeView)

    composeView.post {
        try {
            composeView.measure(
                View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY),/// el ancho siempre va a ser el mismo
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED) /// la altura no se restringe, para poder guardar mas de una pagina
            )
            composeView.layout(0, 0, pageWidth, composeView.measuredHeight)


            val totalHeight = composeView.measuredHeight
            val totalPages = (totalHeight / pageHeight) + if (totalHeight % pageHeight != 0) 1 else 0

            for (pageIndex in 0 until totalPages) {
                val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageIndex + 1).create()
                val page = pdfDocument.startPage(pageInfo)

                val canvas = page.canvas
                val offsetY = -pageIndex * pageHeight

                canvas.save()
                canvas.translate(0f, offsetY.toFloat())
                composeView.draw(canvas)
                canvas.restore()

                pdfDocument.finishPage(page)
            }

            val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val pdfFile = File(downloadsDirectory, "${game.name}.pdf")/// ruta y nombre

            pdfDocument.writeTo(FileOutputStream(pdfFile))
            Toast.makeText(context, context.getString(R.string.pdf_guardado_en, pdfFile.absolutePath), Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, context.getString(R.string.error_al_exportar_pdf, e.localizedMessage), Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()/// hay que cerrar el documento siempre para liberar recursos

            decorView.removeView(composeView) /// quita el composeView temporal que ha sido creada al principio para liberar recursos
        }
    }
}

