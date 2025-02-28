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
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import fr.isen.laval.isensmartcompanion.R


class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val eventTitle = intent.getStringExtra("event_title") ?: "Événement"
        val eventDescription = intent.getStringExtra("event_description") ?: "Aucune description disponible"
        val eventDate = intent.getStringExtra("event_date") ?: "Date inconnue"
        val eventLocation = intent.getStringExtra("event_location") ?: "Lieu inconnu"
        val eventCategory = intent.getStringExtra("event_category") ?: "Catégorie inconnue"


    }
}

@Composable
fun EventDetailScreen(navController: NavController, backStackEntry: NavBackStackEntry, eventsViewModel: EventsViewModel) {
    val eventId = backStackEntry.arguments?.getString("eventId")
    val event = eventsViewModel.events.find { it.id.toString() == eventId }

    Scaffold(
        topBar = { EventDetailTopBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp), // Padding général réduit pour une aération plus grande
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (event == null) {
                Text(
                    text = "Événement introuvable",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF9E9E9E), // Couleur grise claire
                )
            } else {
                EventDetailContent(event)
            }
        }
    }
}

@Composable
fun EventDetailContent(event: Event) {
    Spacer(modifier = Modifier.height(12.dp)) // Espacement plus léger

    // Titre de l'événement
    Text(
        text = event.title,
        fontSize = 30.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFF212121)
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Card contenant les informations sur l'événement
    Card(
        shape = RoundedCornerShape(20.dp), // Coins plus arrondis pour un effet plus doux
        elevation = CardDefaults.cardElevation(12.dp), // Légère élévation
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1)) // Fond très clair
    ) {
        Column(
            modifier = Modifier.padding(24.dp), // Plus de padding pour aérer le contenu
            horizontalAlignment = Alignment.Start
        ) {
            // Affichage des informations détaillées
            DetailItem(label = "Date", value = event.date, icon = Icons.Default.CalendarToday)
            DetailItem(label = "Lieu", value = event.location, icon = Icons.Default.LocationOn)
            // Nouveau changement d'emoji pour catégorie
            DetailItem(label = "Catégorie", value = event.category, icon = Icons.Default.Label)

            Spacer(modifier = Modifier.height(24.dp)) // Espacement pour séparer la description

            // Description de l'événement avec une taille plus grande et plus aérée
            Text(
                text = event.description,
                fontSize = 18.sp,
                color = Color(0xFF616161), // Texte dans une couleur un peu plus douce
                lineHeight = 24.sp // Pour aérer le texte
            )
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp), // Plus d'espace entre les éléments
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF4CAF50), // Icônes en couleur verte pour plus de contraste
            modifier = Modifier.size(24.dp) // Icônes légèrement plus grandes
        )
        Spacer(modifier = Modifier.width(12.dp))

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF757575) // Couleur des labels plus douce
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color(0xFF424242) // Texte de la valeur en gris foncé
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
                Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
            }
        },
        title = {
            Text(
                text = "Détails de l'événement",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121) // Couleur du titre dans un gris foncé
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFAFAFA)) // Fond plus léger
    )
}

fun sendNotification(context: Context, eventTitle: String, eventDescription: String) {
    val channelId = "event_reminders"
    val notificationId = eventTitle.hashCode()


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Rappels d'événements",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications pour les rappels d'événements"
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Rappel : $eventTitle")
        .setContentText(eventDescription)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

  /*  // 🔥 Envoyer la notification
    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, notification)
    }*/
}