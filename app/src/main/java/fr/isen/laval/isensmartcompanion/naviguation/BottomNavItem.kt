package fr.isen.laval.isensmartcompanion.naviguation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home

sealed class BottomNavItem(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : BottomNavItem("home", "Accueil", Icons.Filled.Home)
    object Events : BottomNavItem("events", "Événements", Icons.Filled.Event)
    object History : BottomNavItem("history", "Historique", Icons.Filled.History)
    object Agenda : BottomNavItem("agenda", "Agenda", Icons.Filled.CalendarToday)
}

