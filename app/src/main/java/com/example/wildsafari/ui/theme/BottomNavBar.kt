package com.example.wildsafari.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.wildsafari.ROUTE_HOME
import com.example.wildsafari.ROUTE_ADDANIMAL
import com.example.wildsafari.ROUTE_ANIMALS

@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar {
        // 🏠 Home
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == ROUTE_HOME,
            onClick = { navController.navigate(ROUTE_HOME) }
        )

        // ➕ Add Animals
        NavigationBarItem(
            icon = { Icon(Icons.Default.Add, contentDescription = "Add Animal") },
            label = { Text("Add Animal") },
            selected = currentRoute == ROUTE_ADDANIMAL,
            onClick = { navController.navigate(ROUTE_ADDANIMAL) }
        )

        // 🐆 Animals
        NavigationBarItem(
            icon = { Icon(Icons.Default.Pets, contentDescription = "Animals") },
            label = { Text("Animals") },
            selected = currentRoute == ROUTE_ANIMALS,
            onClick = { navController.navigate(ROUTE_ANIMALS) }
        )

        // 🔔 Notifications (placeholder route)
        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Notifications") },
            label = { Text("Notifications") },
            selected = currentRoute == "notifications",
            onClick = { navController.navigate("notifications") }
        )
    }
}

