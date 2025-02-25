package fr.isen.laval.isensmartcompanion.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun EventsScreen() {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(
            onClick = {
                // Utiliser Intent pour ouvrir l'activité de détails de l'événement
                val intent = Intent(context, EventDetailActivity::class.java)
                context.startActivity(intent)
            }
        ) {
            Text("Voir Détails Événement")
        }
    }
}
