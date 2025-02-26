package fr.isen.laval.isensmartcompanion.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext

@Composable
fun EventsScreen() {
    val events = listOf(
        Event(1, "BDE Evening", "An evening organized by the BDE.", "2025-03-10", "ISEN", "Social"),
        Event(2, "Gala", "A prestigious Gala event.", "2025-05-01", "Le Grand Hôtel", "Formal"),
        Event(3, "Cohesion Day", "A day for team building and fun activities.", "2025-04-15", "ISEN", "Social")
    )

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(events) { event ->
            EventItem(event)
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
                val intent = Intent(context, EventDetailActivity::class.java).apply {
                    putExtra("eventId", event.id)
                    putExtra("eventTitle", event.title)
                    putExtra("eventDescription", event.description)
                    putExtra("eventDate", event.date)
                    putExtra("eventLocation", event.location)
                    putExtra("eventCategory", event.category)
                }
                context.startActivity(intent)
            },
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.title, style = MaterialTheme.typography.titleMedium)
        }
    }
}
