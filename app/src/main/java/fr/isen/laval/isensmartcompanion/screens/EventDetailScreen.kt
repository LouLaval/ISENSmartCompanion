package fr.isen.laval.isensmartcompanion.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import fr.isen.laval.isensmartcompanion.screens.Event
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.LocationOn

@Composable
fun EventDetailScreen(navController: NavController, backStackEntry: NavBackStackEntry, eventsViewModel: EventsViewModel) {
    // Récupérer l'ID de l'événement depuis les arguments de la navigation
    val eventId = backStackEntry.arguments?.getString("eventId")

    // Trouver l'événement dans le ViewModel avec l'ID en tant que String
    val event = eventsViewModel.events.find { it.id.toString() == eventId }

    Scaffold(
        topBar = { EventDetailTopBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (event == null) {
                Text(
                    text = "Événement introuvable",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            } else {
                EventDetailContent(event)
            }
        }
    }
}


@Composable
fun EventDetailContent(event: Event) {
    Spacer(modifier = Modifier.height(16.dp))

    // Titre de l'événement
    Text(
        text = event.title,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF212121)
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Carte contenant les détails de l'événement
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEEEEEE) // Fond
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            DetailItem(label = "Date", value = event.date, icon = Icons.Default.CalendarToday)
            DetailItem(label = "Lieu", value = event.location, icon = Icons.Default.LocationOn)
            DetailItem(label = "Catégorie", value = event.category, icon = Icons.Default.Category)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = event.description,
                fontSize = 18.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.Black, // Icônes en noir
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black // Catégories en gras et noir
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailTopBar(navController: NavController) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
            }
        },
        title = {
            Text(
                text = "Détails de l'événement",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    )
}