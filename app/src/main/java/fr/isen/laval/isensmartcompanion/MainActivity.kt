package fr.isen.laval.isensmartcompanion

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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import fr.isen.laval.isensmartcompanion.ui.theme.ISENSmartCompanionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ISENSmartCompanionTheme {
                AssistantScreen()
            }
        }
    }
}

@Composable
fun AssistantScreen() {
    var question by remember { mutableStateOf(TextFieldValue("")) }
    var response by remember { mutableStateOf("Posez-moi une question !") }
    val context = LocalContext.current // Permet d'afficher un Toast

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
        Text(
            text = response,
            fontSize = 16.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(16.dp)
        )

        // Input Field with Send Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFE0E0E0)) // Fond gris clair pour mieux coller au design
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = question,
                onValueChange = { question = it },
                placeholder = { Text("Écrivez votre question...") }, // Champ vide comme sur l'image
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

            // Send Button with Material Icon
            IconButton(
                onClick = {
                    Toast.makeText(context, "Question Submitted", Toast.LENGTH_SHORT).show()
                    response = "Vous avez demandé : ${question.text}" // Change le texte de réponse
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
