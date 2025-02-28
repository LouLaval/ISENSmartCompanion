package fr.isen.laval.isensmartcompanion.screens

import android.os.Bundle
import android.widget.CalendarView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import java.text.SimpleDateFormat
import java.util.*

class AgendaScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgendaScreenContent()
        }
    }
}

@Composable
fun AgendaScreenContent() {
    var selectedDate by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var eventName by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }

    // Sauvegarde des événements (clé = date, valeur = liste d'événements)
    val events = remember { mutableStateMapOf<String, MutableList<Pair<String, String>>>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Affichage de la date sélectionnée
        Text(
            text = if (selectedDate.isNotEmpty()) "📅 Date sélectionnée : $selectedDate" else "📅 Aucune date sélectionnée",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Intégration de CalendarView
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.LightGray),
            factory = { ctx ->
                CalendarView(ctx).apply {
                    setOnDateChangeListener { _, year, month, dayOfMonth ->
                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, dayOfMonth)
                        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("fr", "FR"))
                        selectedDate = dateFormat.format(calendar.time)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton pour ajouter un événement
        Button(
            onClick = { showDialog = true },
            enabled = selectedDate.isNotEmpty()
        ) {
            Text(text = "Ajouter un événement")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Encadré affichant les événements pour la date sélectionnée
        if (events[selectedDate] != null && events[selectedDate]!!.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📅 Événements du $selectedDate",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )

                    LazyColumn {
                        items(events[selectedDate] ?: emptyList()) { event ->
                            Text(
                                text = "🔹 ${event.first} " +
                                        "📍localisation : ${event.second}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialog pour entrer un événement
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Ajouter un événement") },
            text = {
                Column {
                    Text(text = "Nom de l'événement :")
                    OutlinedTextField(
                        value = eventName,
                        onValueChange = { eventName = it },
                        placeholder = { Text("Ex: Réunion") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Localisation :")
                    OutlinedTextField(
                        value = eventLocation,
                        onValueChange = { eventLocation = it },
                        placeholder = { Text("Ex: Bureau") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (selectedDate.isNotEmpty() && eventName.isNotEmpty() && eventLocation.isNotEmpty()) {
                            events.getOrPut(selectedDate) { mutableListOf() }
                                .add(Pair(eventName, eventLocation))
                        }
                        eventName = ""
                        eventLocation = ""
                        showDialog = false
                    }
                ) {
                    Text("Ajouter")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAgendaScreen() {
    AgendaScreenContent()
}