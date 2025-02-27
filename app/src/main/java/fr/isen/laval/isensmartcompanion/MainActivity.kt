package fr.isen.laval.isensmartcompanion

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import fr.isen.laval.isensmartcompanion.screens.EventDetailScreen
import fr.isen.laval.isensmartcompanion.screens.EventsScreen
import fr.isen.laval.isensmartcompanion.screens.HistoryScreen
import fr.isen.laval.isensmartcompanion.screens.AssistantScreen
import fr.isen.laval.isensmartcompanion.screens.EventsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.laval.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import fr.isen.laval.isensmartcompanion.ai.GeminiApiHelper
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.lifecycleScope
import fr.isen.laval.isensmartcompanion.notif.NotificationReceiver
import kotlinx.coroutines.launch



class MainActivity : ComponentActivity() {
    private lateinit var geminiApiHelper: GeminiApiHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        geminiApiHelper = GeminiApiHelper(this)

        setContent {
            ISENSmartCompanionTheme {
                MainScreen(geminiApiHelper) // Passer geminiApiHelper ici
            }
        }
    }
}

@Composable
fun MainScreen(geminiApiHelper: GeminiApiHelper) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavigationGraph(navController, geminiApiHelper) // Passer geminiApiHelper
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

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Events : Screen("events", "Events", Icons.Filled.Event)
    object History : Screen("history", "History", Icons.Filled.History)
}

@Composable
fun NavigationGraph(navController: NavHostController, geminiApiHelper: GeminiApiHelper) {
    val eventsViewModel: EventsViewModel = viewModel()

    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { AssistantScreen(geminiApiHelper) }
        composable(Screen.Events.route) {
            EventsScreen(navController, eventsViewModel)
        }
        composable(Screen.History.route) { HistoryScreen() }

        // Page de détails d'un événement
        composable("eventDetail/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            EventDetailScreen(navController, backStackEntry, eventsViewModel)
        }
    }
}

fun scheduleNotification(context: Context) {
    val intent = Intent(context, NotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val triggerTime = System.currentTimeMillis() + 10_000 // 10 secondes plus tard

    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
}

