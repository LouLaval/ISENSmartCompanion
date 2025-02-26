package fr.isen.laval.isensmartcompanion.screens

import fr.isen.laval.isensmartcompanion.screens.Event
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun EventsScreen(navController: NavController, eventsViewModel: EventsViewModel) { // ✅ Ajout du paramètre
    val events by remember { derivedStateOf { eventsViewModel.events } }

    Scaffold(
        topBar = { EventsTopBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(events) { event ->
                    EventItem(event, navController)
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsTopBar() {
    TopAppBar(
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ISEN",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB71C1C) // Rouge foncé
                )
                Text(
                    text = "Smart Companion",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    )
}

@Composable
fun EventItem(event: Event, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate("eventDetail/${event.id}")
            },
        shape = RoundedCornerShape(12.dp), // Coins arrondis
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEBEE) // Fond rouge clair uniforme
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Affichage du titre de l'événement
            Text(
                text = event.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB71C1C) // Rouge foncé
            )
        }
    }
}