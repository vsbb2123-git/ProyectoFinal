package com.vsantamaria.proyectofinal.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONObject
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType

suspend fun translateText(text: String, targetLanguage: String = "es"): String {
    val apiKey = "AIzaSyCVGRMEMzNFxbwxskVKLDjqkt0P6pTWbLA"
    val url = "https://translation.googleapis.com/language/translate/v2?key=$apiKey"

    val requestBody = """
        {
            "q": "$text",
            "target": "$targetLanguage"
        }
    """.trimIndent()

    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .post(requestBody.toRequestBody("application/json".toMediaType()))
        .build()

    return withContext(Dispatchers.IO) {
        val response = client.newCall(request).execute()
        val jsonResponse = response.body?.string() ?: return@withContext text
        val translatedText = JSONObject(jsonResponse)
            .getJSONObject("data")
            .getJSONArray("translations")
            .getJSONObject(0)
            .getString("translatedText")

        translatedText
    }
}
