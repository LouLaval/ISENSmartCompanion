package fr.isen.laval.isensmartcompanion.screens

data class Event(
    val id: String, //car l'API envoie un id sous forme de texte
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
)
