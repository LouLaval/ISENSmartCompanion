package fr.isen.laval.isensmartcompanion.screens

import android.content.Context
import android.os.Bundle
import android.widget.CalendarView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import fr.isen.laval.isensmartcompanion.data.DataStoreManager
import androidx.compose.material.icons.filled.Delete



class AgendaScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStoreManager = DataStoreManager(applicationContext)

        setContent {
            AgendaScreenContent(dataStoreManager)
        }
    }
}

@Composable
fun AgendaScreenContent(dataStoreManager: DataStoreManager) {
    var selectedDate by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var eventName by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }

    val events = remember { mutableStateMapOf<String, MutableList<Pair<String, String>>>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        dataStoreManager.eventsFlow.collect { savedEvents ->
            events.clear()
            events.putAll(savedEvents.mapValues { it.value.toMutableList() })
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (selectedDate.isNotEmpty()) " Date sélectionnée : $selectedDate" else " Aucune date sélectionnée",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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
                        val dateFormat = SimpleDateFormat("dd MMMM பிரபு", Locale("fr", "FR"))
                        selectedDate = dateFormat.format(calendar.time)
                        println("Date sélectionnée : $selectedDate")
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                println("Bouton cliqué, showDialog avant : $showDialog")
                showDialog = true
                println("Bouton cliqué, showDialog apres : $showDialog")
            },
            enabled = selectedDate.isNotEmpty()
        ) {
            Text(text = "Ajouter un événement")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (events[selectedDate]?.isNotEmpty() == true) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(25.dp)) {
                    Text(
                        text = " Événements du $selectedDate",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF37474F),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn {
                        itemsIndexed(events[selectedDate] ?: emptyList()) { index, event ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Localisation",
                                    tint = Color(0xFF26A69A),
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
                                        text = event.second,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF616161)
                                    )
                                }
                            }
                            IconButton(onClick = {
                                events[selectedDate]?.removeAt(index)
                                coroutineScope.launch {
                                    dataStoreManager.saveEvent(selectedDate, events[selectedDate] ?: emptyList())
                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Supprimer")
                            }
                        }
                    }
                }
            }
        }
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
                            println("eventName avant sauvegarde: $eventName")
                            println("eventLocation avant sauvegarde: $eventLocation")
                            if (selectedDate.isNotEmpty() && eventName.isNotEmpty() && eventLocation.isNotEmpty()) {
                                val newEvent = Pair(eventName, eventLocation)
                                if (events[selectedDate] == null) {
                                    events[selectedDate] = mutableListOf()
                                }
                                events[selectedDate]?.add(newEvent)
                                println("events avant sauvegarde: $events")
                                coroutineScope.launch {
                                    try {
                                        dataStoreManager.saveEvent(selectedDate, events[selectedDate] ?: emptyList())
                                        println("Événement sauvegardé dans DataStore")
                                        println("events après sauvegarde: $events")
                                    } catch (e: Exception) {
                                        println("Erreur lors de la sauvegarde : ${e.message}")
                                    }
                                }
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
}