package fr.isen.laval.isensmartcompanion.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey


// Extension pour créer DataStore
val Context.dataStore by preferencesDataStore(name = "event_preferences")

class DataStoreManager(private val context: Context) {

    companion object {
        private val EVENTS_KEY = stringPreferencesKey("events")
    }

    // Lire les événements sauvegardés
    val eventsFlow: Flow<Map<String, List<Pair<String, String>>>> = context.dataStore.data
        .map { preferences ->
            preferences[EVENTS_KEY]?.let { json ->
                deserializeEvents(json)
            } ?: emptyMap()
        }

    // Sauvegarder un nouvel événement
    suspend fun saveEvent(date: String, eventName: String, eventLocation: String) {
        context.dataStore.edit { preferences ->
            val currentEvents = preferences[EVENTS_KEY]?.let { deserializeEvents(it) } ?: mutableMapOf()
            val updatedEvents = currentEvents.toMutableMap().apply {
                put(date, (this[date] ?: mutableListOf()) + (eventName to eventLocation))
            }
            preferences[EVENTS_KEY] = serializeEvents(updatedEvents)
        }
    }

    // Convertir les événements en JSON pour les sauvegarder
    private fun serializeEvents(events: Map<String, List<Pair<String, String>>>): String {
        return events.entries.joinToString(";") { "${it.key}:${it.value.joinToString(",") { e -> "${e.first}|${e.second}" }}" }
    }

    // Convertir le JSON en structure de données utilisable
    private fun deserializeEvents(data: String): Map<String, List<Pair<String, String>>> {
        return data.split(";").mapNotNull { entry ->
            val parts = entry.split(":")
            if (parts.size == 2) {
                val date = parts[0]
                val eventList = parts[1].split(",").mapNotNull { event ->
                    val details = event.split("|")
                    if (details.size == 2) details[0] to details[1] else null
                }
                date to eventList
            } else {
                null
            }
        }.toMap()
    }
}
