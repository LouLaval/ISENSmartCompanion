package fr.isen.laval.isensmartcompanion.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext

@Composable
fun EventsScreen() {
    // Liste d'événements factices
    val events = listOf(
        Event(1, "BDE Evening", "An evening organized by the BDE.", "2025-03-10", "ISEN Laval", "Social"),
        Event(2, "Gala", "A prestigious Gala event.", "2025-05-01", "Le Grand Hôtel", "Formal"),
        Event(3, "Cohesion Day", "A day for team building and fun activities.", "2025-04-15", "ISEN Laval Campus", "Social")
    )

    // LazyColumn pour afficher la liste des événements
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(events) { event ->
            EventItem(event) // Composant pour chaque événement
        }
    }
}

@Composable
fun EventItem(event: Event) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                // Lancer EventDetailActivity avec l'ID de l'événement
                val intent = Intent(context, EventDetailActivity::class.java).apply {
                    putExtra("eventId", event.id) // Passer l'ID de l'événement
                }
                context.startActivity(intent)
            },
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = event.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date: ${event.date}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Location: ${event.location}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Category: ${event.category}", style = MaterialTheme.typography.bodySmall)
        }
    }
}