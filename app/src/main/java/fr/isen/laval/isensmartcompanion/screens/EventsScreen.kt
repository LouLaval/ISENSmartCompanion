package fr.isen.laval.isensmartcompanion.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController



@Composable
fun EventsScreen(navController: NavController, eventsViewModel: EventsViewModel) {
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
                Text(text = "ISEN", fontSize = 40.sp, color = Color.Red, textAlign = TextAlign.Center)
                Text(text = "Smart Companion", fontSize = 20.sp, color = Color.Gray, textAlign = TextAlign.Center)
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
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEEEEEE)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = event.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121),
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Voir les détails",
                tint = Color(0xFF616161)
            )
        }
    }
}