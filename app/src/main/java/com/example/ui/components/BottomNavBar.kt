package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun BottomNavBar(
    currentScreen: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "خانه") },
            label = { Text("خانه") },
            selected = currentScreen == "home",
            onClick = { onNavigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "صلوات") },
            label = { Text("صلوات") },
            selected = currentScreen == "salawat",
            onClick = { onNavigate("salawat") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.List, contentDescription = "اذکار") },
            label = { Text("اذکار") },
            selected = currentScreen == "zekrs",
            onClick = { onNavigate("zekrs") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.BarChart, contentDescription = "آمار") },
            label = { Text("آمار") },
            selected = currentScreen == "stats",
            onClick = { onNavigate("stats") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.CalendarMonth, contentDescription = "تقویم") },
            label = { Text("تقویم") },
            selected = currentScreen == "calendar",
            onClick = { onNavigate("calendar") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.History, contentDescription = "تاریخچه") },
            label = { Text("تاریخچه") },
            selected = currentScreen == "history",
            onClick = { onNavigate("history") }
        )
    }
}
