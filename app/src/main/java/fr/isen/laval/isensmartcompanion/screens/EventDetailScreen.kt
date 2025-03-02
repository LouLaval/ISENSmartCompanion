package fr.isen.laval.isensmartcompanion.screens


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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.laval.isensmartcompanion.R
import fr.isen.laval.isensmartcompanion.notif.NotificationViewModel



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
fun EventDetailScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    eventsViewModel: EventsViewModel,
    notificationViewModel: NotificationViewModel = viewModel()
) {
    val eventId = backStackEntry.arguments?.getString("eventId")
    val event = eventsViewModel.events.find { it.id.toString() == eventId }
    val context = LocalContext.current

    Scaffold(
        topBar = { EventDetailTopBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (event == null) {
                Text(
                    text = "Événement introuvable",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            } else {
                EventDetailContent(event, notificationViewModel, context)
            }
        }
    }
}

@Composable
fun EventDetailContent(event: Event, notificationViewModel: NotificationViewModel, context: Context) {
    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = event.title,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF37474F)
    )

    Spacer(modifier = Modifier.height(16.dp))

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            DetailItem(label = "Date", value = event.date, icon = Icons.Default.CalendarToday)
            DetailItem(label = "Lieu", value = event.location, icon = Icons.Default.LocationOn)
            DetailItem(label = "Catégorie", value = event.category, icon = Icons.Default.Label)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = event.description,
                fontSize = 16.sp,
                color = Color(0xFF546E7A),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { notificationViewModel.sendNotification(context, event.title, event.description) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB0BEC5))
            ) {
                Text(
                    text = "🔔 Activer un rappel",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFFB0BEC5),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF525252)
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color(0xFF2E2E2E)
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
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E2E2E)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFB0BEC5))
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

}