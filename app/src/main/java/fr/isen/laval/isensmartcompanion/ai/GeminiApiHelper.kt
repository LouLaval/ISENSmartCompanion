package fr.isen.laval.isensmartcompanion.ai

import android.content.Context
import com.google.ai.client.generativeai.GenerativeModel
import fr.isen.laval.isensmartcompanion.getApiKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiApiHelper(context: Context) {
    private val apiKey = getApiKey(context)
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey
    )

    suspend fun analyzeText(userInput: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(userInput)
                response.text ?: "Aucune réponse trouvée."
            } catch (e: Exception) {
                "Erreur : ${e.message}"
            }
        }
    }
}

