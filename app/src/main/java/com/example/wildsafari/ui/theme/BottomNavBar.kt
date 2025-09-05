package com.example.wildsafari.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Tour
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.wildsafari.ROUTE_HOME
import com.example.wildsafari.ROUTE_ANIMALS
import com.example.wildsafari.ROUTE_PLACES
import com.example.wildsafari.ROUTE_TOURS

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

        // 🐆 Animals
        NavigationBarItem(
            icon = { Icon(Icons.Default.Pets, contentDescription = "Animals") },
            label = { Text("Animals") },
            selected = currentRoute == ROUTE_ANIMALS,
            onClick = { navController.navigate(ROUTE_ANIMALS) }
        )

        // 📍 Places
        NavigationBarItem(
            icon = { Icon(Icons.Default.Place, contentDescription = "Places") },
            label = { Text("Places") },
            selected = currentRoute == ROUTE_PLACES,
            onClick = { navController.navigate(ROUTE_PLACES) }
        )

        // 🗺️ Tours
        NavigationBarItem(
            icon = { Icon(Icons.Default.Tour, contentDescription = "Tours") },
            label = { Text("Tours") },
            selected = currentRoute == ROUTE_TOURS,
            onClick = { navController.navigate(ROUTE_TOURS) }
        )
    }
}
