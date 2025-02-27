package fr.isen.laval.isensmartcompanion.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import fr.isen.laval.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import fr.isen.laval.isensmartcompanion.ai.GeminiApiHelper
import kotlinx.coroutines.launch
import com.google.ai.client.generativeai.GenerativeModel
import fr.isen.laval.isensmartcompanion.data.InteractionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    private lateinit var geminiApiHelper: GeminiApiHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialiser GeminiApiHelper
        geminiApiHelper = GeminiApiHelper(this)

        // Passer geminiApiHelper à AssistantScreen
        setContent {
            ISENSmartCompanionTheme {
                AssistantScreen(geminiApiHelper)
            }
        }
    }
}

@Composable
fun AssistantScreen(geminiApiHelper: GeminiApiHelper, viewModel: InteractionViewModel = viewModel()) {
    var question by remember { mutableStateOf("") }
    var lastQuestion by remember { mutableStateOf<String?>(null) }
    var response by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val generativeModel = GenerativeModel("gemini-1.5-flash", "AIzaSyDnF7yGPomooqLkOQ77nfoXbxI0z1xjO-k")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Title
        Text(
            text = "ISEN",
            fontSize = 40.sp,
            color = Color.Red,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Smart Companion",
            fontSize = 20.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        // Response Text
        response?.let {
            Text(
                text = it,
                fontSize = 16.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Input Field with Send Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFE0E0E0))
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = question,
                onValueChange = { question = it },
                placeholder = { Text("Écrivez votre question...") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFE0E0E0),
                    unfocusedContainerColor = Color(0xFFE0E0E0),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Send Button
            IconButton(
                onClick = {
                    if (question.isNotBlank()) {
                        Toast.makeText(context, "Analyse de la question...", Toast.LENGTH_SHORT).show()

                        coroutineScope.launch {
                            response = getAIResponse(generativeModel, question)
                        }

                        question = "" // Réinitialiser le champ de texte
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.Red)
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Envoyer",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

// 🔹 **Fonction pour interroger Gemini AI**
private suspend fun getAIResponse(generativeModel: GenerativeModel, input: String): String {
    return try {
        val response = generativeModel.generateContent(input) // Vérifier cette ligne selon l'API exacte
        response.text ?: "Aucune réponse obtenue"
    } catch (e: Exception) {
        "Erreur: ${e.message}"
    }
}
