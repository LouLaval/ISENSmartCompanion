package fr.isen.laval.isensmartcompanion.screens

import java.util.*

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
)
