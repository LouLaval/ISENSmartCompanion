package fr.isen.laval.isensmartcompanion.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text // Material 3 import
import androidx.compose.material3.TopAppBar // Material 3 import
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api // Import pour activer les APIs expérimentales

@OptIn(ExperimentalMaterial3Api::class) // Désactive l'avertissement pour l'API expérimentale
@Composable
fun AgendaScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text(text = "Mon Agenda") })

        // Contenu principal
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Bienvenue dans mon agenda",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
fun PreviewAgendaScreen() {
    AgendaScreen()
}
