package fr.isen.laval.isensmartcompanion

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import fr.isen.laval.isensmartcompanion.screens.EventDetailActivity
import fr.isen.laval.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import  fr.isen.laval.isensmartcompanion.screens.AssistantScreen
import  fr.isen.laval.isensmartcompanion.screens.EventsScreen
import  fr.isen.laval.isensmartcompanion.screens.HistoryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ISENSmartCompanionTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavigationGraph(navController)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.Home,
        Screen.Events,
        Screen.History
    )

    NavigationBar(containerColor = Color.White) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { AssistantScreen() }
        composable(Screen.Events.route) { EventsScreen() }
        composable(Screen.History.route) { HistoryScreen() }
    }
}

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Events : Screen("events", "Events", Icons.Filled.Event)
    object History : Screen("history", "History", Icons.Filled.History)
}
/*
@Composable
fun AssistantScreen() {
    // Ton code ici pour AssistantScreen
}

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

@Composable
fun HistoryScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Historique des interactions", fontSize = 20.sp)
    }
}
*/