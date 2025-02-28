package fr.isen.laval.isensmartcompanion.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Main Activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AgendaScreen()
                }
            }
        }
    }
}

@Composable
fun AgendaScreen() {
    // Afficher simplement "Bienvenue"
    Text(
        text = "Bienvenue",
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAgendaScreen() {
    AgendaScreen()
}

