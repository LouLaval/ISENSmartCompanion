package fr.isen.laval.isensmartcompanion.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey

val Context.dataStore by preferencesDataStore(name = "event_preferences")

class DataStoreManager(private val context: Context) {

    companion object {
        private val EVENTS_KEY = stringPreferencesKey("events")
    }

    val eventsFlow: Flow<Map<String, List<Pair<String, String>>>> = context.dataStore.data
        .map { preferences ->
            preferences[EVENTS_KEY]?.let { json ->
                deserializeEvents(json)
            } ?: emptyMap()
        }

    suspend fun saveEvent(date: String, events: List<Pair<String, String>>) {
        context.dataStore.edit { preferences ->
            val currentEvents = preferences[EVENTS_KEY]?.let { deserializeEvents(it) } ?: mutableMapOf()
            val updatedEvents = currentEvents.toMutableMap().apply {
                put(date, events)
            }

            println("Événements avant sauvegarde: $updatedEvents")
            val serializedEvents = serializeEvents(updatedEvents)
            println("Événements sérialisés: $serializedEvents")
            preferences[EVENTS_KEY] = serializedEvents
        }
    }

    private fun serializeEvents(events: Map<String, List<Pair<String, String>>>): String {
        println("Sérialisation en cours : $events")
        val serialized = events.entries.joinToString(";") { entry ->
            "${entry.key}:${entry.value.joinToString(",", transform = { "${it.first}|${it.second}" })}"
        }
        println("Chaîne sérialisée : $serialized")
        return serialized
    }

    private fun deserializeEvents(data: String): Map<String, List<Pair<String, String>>> {
        println("Données avant désérialisation: $data")
        return data.split(";").filter { it.isNotEmpty() }.associate { entry ->
            val (date, eventsString) = entry.split(":", limit = 2)
            val events = eventsString.split(",").filter { it.isNotEmpty() }.map { event ->
                val (name, location) = event.split("|", limit = 2)
                name to location
            }
            date to events
        }
    }
}