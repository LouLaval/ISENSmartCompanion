package fr.isen.laval.isensmartcompanion

import android.content.Context
import java.io.FileInputStream
import java.util.Properties
import fr.isen.laval.isensmartcompanion.getApiKey


fun getApiKey(context: Context): String {
    val properties = Properties()
    try {
        // Ouvrir le fichier local.properties à partir du répertoire de l'application
        val fileInputStream = FileInputStream("${context.filesDir}/local.properties")
        properties.load(fileInputStream)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    // Retourner la clé API depuis les propriétés
    return properties.getProperty("GEMINI_API_KEY", "")
}
