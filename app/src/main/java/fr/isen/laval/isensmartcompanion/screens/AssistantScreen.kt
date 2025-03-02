package fr.isen.laval.isensmartcompanion.screens

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import kotlinx.coroutines.launch
import com.google.ai.client.generativeai.GenerativeModel
import fr.isen.laval.isensmartcompanion.data.InteractionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

@Composable
fun AssistantScreen(viewModel: InteractionViewModel = viewModel()) {
    var question by remember { mutableStateOf("") }
    var aiResponse by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val generativeModel = GenerativeModel("gemini-1.5-flash", "AIzaSyDnF7yGPomooqLkOQ77nfoXbxI0z1xjO-k")


    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Logo et titre
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

            Spacer(modifier = Modifier.height(20.dp))

            // Affichage de la réponse
            if (aiResponse.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                        .padding(12.dp)
                ) {
                    Text(" $aiResponse", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Barre de saisie et bouton d'envoi
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
                            val userQuestion = question

                            coroutineScope.launch(Dispatchers.IO) {
                                val response = getAIResponse(generativeModel, userQuestion)

                                withContext(Dispatchers.Main) {
                                    aiResponse = response
                                    question = ""
                                }

                                viewModel.insertInteraction(userQuestion, response)
                            }
                        } else {
                            Toast.makeText(context, "Veuillez entrer une question", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                ) {
                    Icon(imageVector = Icons.Filled.Send, contentDescription = "Envoyer", tint = Color.White)
                }

            }

            Spacer(modifier = Modifier.height(20.dp))
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
