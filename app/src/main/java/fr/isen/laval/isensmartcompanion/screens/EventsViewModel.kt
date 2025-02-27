package fr.isen.laval.isensmartcompanion.screens

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import fr.isen.laval.isensmartcompanion.screens.Event
import fr.isen.laval.isensmartcompanion.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class EventsViewModel : ViewModel() {
    private val _events = mutableStateListOf<Event>() // ✅ Liste qui se met à jour dynamiquement
    val events: List<Event> get() = _events

    init {
        fetchEvents() // ✅ Charge les événements dès le démarrage
    }

    fun fetchEvents() {
        // Vérifiez si RetrofitInstance est bien importé et accessible
        RetrofitInstance.api.getEvents().enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _events.clear()  // Efface les anciens événements
                        _events.addAll(it) // Ajoute les nouveaux événements
                    }
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                Log.e("EventsViewModel", "Erreur de récupération des événements", t)
            }
        })
    }
}

