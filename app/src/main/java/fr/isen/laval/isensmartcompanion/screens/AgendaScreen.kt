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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
        if (events[selectedDate]?.isNotEmpty() == true) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)), // Gris clair minimaliste
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(25.dp)) {
                    Text(
                        text = "📅 Événements du $selectedDate",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF37474F), // Gris foncé élégant
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn {
                        items(events[selectedDate] ?: emptyList()) { event ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Localisation",
                                    tint = Color(0xFF26A69A), // Vert doux
                                    modifier = Modifier.size(25.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = event.first,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF212121)
                                    )
                                    Text(
                                        text = "${event.second}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF616161)
                                    )
                                }
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
/*
@Preview(showBackground = true)
@Composable
fun PreviewAgendaScreen() {
    AgendaScreenContent()
}*/
}
