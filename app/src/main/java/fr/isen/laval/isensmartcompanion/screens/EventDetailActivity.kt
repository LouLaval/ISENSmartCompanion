package fr.isen.laval.isensmartcompanion.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val eventTitle = intent.getStringExtra("eventTitle") ?: "No Title"
            val eventDescription = intent.getStringExtra("eventDescription") ?: "No Description"
            val eventDate = intent.getStringExtra("eventDate") ?: "No Date"
            val eventLocation = intent.getStringExtra("eventLocation") ?: "No Location"
            val eventCategory = intent.getStringExtra("eventCategory") ?: "No Category"

            EventDetailScreen(eventTitle, eventDescription, eventDate, eventLocation, eventCategory)
        }
    }
}

@Composable
fun EventDetailScreen(
    eventTitle: String,
    eventDescription: String,
    eventDate: String,
    eventLocation: String,
    eventCategory: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = eventTitle, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Description: $eventDescription", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Date: $eventDate", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Location: $eventLocation", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Category: $eventCategory", style = MaterialTheme.typography.bodySmall)
    }
}
