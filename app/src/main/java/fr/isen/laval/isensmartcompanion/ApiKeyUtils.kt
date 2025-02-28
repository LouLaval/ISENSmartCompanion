package fr.isen.laval.isensmartcompanion

import android.content.Context
import java.io.FileInputStream
import java.util.Properties
import fr.isen.laval.isensmartcompanion.getApiKey


fun getApiKey(context: Context): String {
    val properties = Properties()
    try {
        val fileInputStream = FileInputStream("${context.filesDir}/local.properties")
        properties.load(fileInputStream)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return properties.getProperty("GEMINI_API_KEY", "")
}
