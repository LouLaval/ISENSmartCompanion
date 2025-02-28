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
import androidx.compose.foundation.shape.CircleShape
import fr.isen.laval.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import fr.isen.laval.isensmartcompanion.ai.GeminiApiHelper
import kotlinx.coroutines.launch
import com.google.ai.client.generativeai.GenerativeModel
import fr.isen.laval.isensmartcompanion.data.InteractionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers


/*class MainActivity : ComponentActivity() {

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
*/

@Composable
fun AssistantScreen(viewModel: InteractionViewModel = viewModel()) {
    var question by remember { mutableStateOf("") } // Question entrée par l'utilisateur
    var aiResponse by remember { mutableStateOf("") } // Réponse de l'IA
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val interactionHistory by viewModel.allInteractions.collectAsState(initial = emptyList())

    val generativeModel = GenerativeModel("gemini-1.5-flash", "AIzaSyBguWA9SSbLDlRrO6e5RZo3WoZkPpEl7as")

    Box(modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(text = "ISEN", fontSize = 40.sp, color = Color.Red, textAlign = TextAlign.Center)
        Text(text = "Smart Companion", fontSize = 20.sp, color = Color.Gray, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.weight(1f))


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
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (question.isNotEmpty()) {
                        coroutineScope.launch(Dispatchers.IO) {
                            aiResponse = getAIResponse(generativeModel, question)

                            viewModel.insertInteraction(question, aiResponse)
                        }
                    } else {
                        Toast.makeText(context, "Veuillez entrer une question", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.Red)
            ) {
                Icon(imageVector = Icons.Filled.Send, contentDescription = "Envoyer", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}


    if (aiResponse.isNotEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFE3F2FD), CircleShape)
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text("❓ $question", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFFFF9C4), CircleShape)
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text("🤖 $aiResponse", fontSize = 16.sp)
            }
        }
    }
}

private suspend fun getAIResponse(generativeModel: GenerativeModel, input: String): String {
    return try {
        println("🔍 Question envoyée : $input")
        val response = generativeModel.generateContent(input).text
        println("📝 Réponse brute reçue : $response")

        response ?: "Aucune réponse obtenue"
    } catch (e: Exception) {
        "Erreur: ${e.message}"
    }
}

