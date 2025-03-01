package fr.isen.laval.isensmartcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.isen.laval.isensmartcompanion.screens.EventDetailScreen
import fr.isen.laval.isensmartcompanion.screens.EventsScreen
import fr.isen.laval.isensmartcompanion.screens.HistoryScreen
import fr.isen.laval.isensmartcompanion.screens.AssistantScreen
import fr.isen.laval.isensmartcompanion.screens.AgendaScreenContent
import fr.isen.laval.isensmartcompanion.screens.EventsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import fr.isen.laval.isensmartcompanion.data.DataStoreManager
import fr.isen.laval.isensmartcompanion.ui.theme.ISENSmartCompanionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStoreManager = DataStoreManager(applicationContext)

        setContent {
            ISENSmartCompanionTheme {
                MainScreen(dataStoreManager)
            }
        }
    }
}

@Composable
fun MainScreen(dataStoreManager: DataStoreManager) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavigationGraph(navController, dataStoreManager)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.Home,
        Screen.Events,
        Screen.History,
        Screen.Agenda
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

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Accueil", Icons.Filled.Home)
    object Events : Screen("events", "Evènements", Icons.Filled.Event)
    object History : Screen("history", "Historique", Icons.Filled.History)
    object Agenda : Screen("agenda", "Agenda", Icons.Filled.CalendarToday)
}

@Composable
fun NavigationGraph(navController: NavHostController, dataStoreManager: DataStoreManager) {
    val eventsViewModel: EventsViewModel = viewModel()

    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { AssistantScreen() }
        composable(Screen.Events.route) {
            EventsScreen(navController, eventsViewModel)
        }
        composable(Screen.History.route) { HistoryScreen() }
        composable(Screen.Agenda.route) {
            AgendaScreenContent(dataStoreManager)  // Passer DataStoreManager ici
        }
        composable("eventDetail/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            EventDetailScreen(navController, backStackEntry, eventsViewModel)
        }
    }
}
