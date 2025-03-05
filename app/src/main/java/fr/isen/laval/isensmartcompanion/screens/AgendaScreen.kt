package fr.isen.laval.isensmartcompanion.screens


import android.os.Bundle
import android.widget.CalendarView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import fr.isen.laval.isensmartcompanion.data.DataStoreManager
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.draw.shadow


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
    var showEditDialog by remember { mutableStateOf(false) }
    var eventName by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var eventTime by remember { mutableStateOf("") }
    var editingEventIndex by remember { mutableStateOf(-1) }

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
                        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("fr", "FR"))
                        selectedDate = dateFormat.format(calendar.time)
                        println("Date sélectionnée : $selectedDate")
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                showDialog = true
            },
            enabled = selectedDate.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .height(56.dp)
                .padding(horizontal = 16.dp)
                .shadow(4.dp, RoundedCornerShape(50.dp))
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Ajouter", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Ajouter un événement", color = Color.White)
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
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Category,
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

                                Spacer(modifier = Modifier.weight(1f))


                                IconButton(onClick = {
                                    eventName = event.first
                                    eventLocation = event.second.split(" à ")[0]
                                    eventTime = event.second.split(" à ")[1]
                                    editingEventIndex = index
                                    showEditDialog = true
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Modifier")
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
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "Heure de l'événement :")
                        OutlinedTextField(
                            value = eventTime,
                            onValueChange = { eventTime = it },
                            placeholder = { Text("Ex: 14:30") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (selectedDate.isNotEmpty() && eventName.isNotEmpty() && eventLocation.isNotEmpty() && eventTime.isNotEmpty()) {
                                val newEvent = Pair(eventName, "$eventLocation à $eventTime")
                                if (events[selectedDate] == null) {
                                    events[selectedDate] = mutableListOf()
                                }
                                events[selectedDate]?.add(newEvent)
                                coroutineScope.launch {
                                    dataStoreManager.saveEvent(selectedDate, events[selectedDate] ?: emptyList())
                                }
                            }
                            eventName = ""
                            eventLocation = ""
                            eventTime = ""
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF26A69A))
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


        if (showEditDialog) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text(text = "Modifier l'événement") },
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
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "Heure de l'événement :")
                        OutlinedTextField(
                            value = eventTime,
                            onValueChange = { eventTime = it },
                            placeholder = { Text("Ex: 14:30") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (selectedDate.isNotEmpty() && eventName.isNotEmpty() && eventLocation.isNotEmpty() && eventTime.isNotEmpty()) {
                                val updatedEvent = Pair(eventName, "$eventLocation à $eventTime")
                                events[selectedDate]?.set(editingEventIndex, updatedEvent)
                                coroutineScope.launch {
                                    dataStoreManager.saveEvent(selectedDate, events[selectedDate] ?: emptyList())
                                }
                            }
                            eventName = ""
                            eventLocation = ""
                            eventTime = ""
                            showEditDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF26A69A))
                    ) {
                        Text("Modifier")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) {
                        Text("Annuler")
                    }
                }
            )
        }
    }
}


