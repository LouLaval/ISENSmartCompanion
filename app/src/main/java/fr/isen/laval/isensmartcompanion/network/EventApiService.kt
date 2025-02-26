package fr.isen.laval.isensmartcompanion.network

import fr.isen.laval.isensmartcompanion.screens.Event
import retrofit2.Call
import retrofit2.http.GET


interface EventApiService {
    @GET("events.json")  // L'URL est relative à la base de l'API
    fun getEvents(): Call<List<Event>>  // Retourne une carte d'événements
}