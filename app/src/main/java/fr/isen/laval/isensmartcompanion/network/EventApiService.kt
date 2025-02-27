package fr.isen.laval.isensmartcompanion.network

import fr.isen.laval.isensmartcompanion.screens.Event
import retrofit2.Call
import retrofit2.http.GET

interface EventApiService {
    @GET("events.json")
    fun getEvents(): Call<List<Event>>
}