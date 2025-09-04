package com.example.wildsafari.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wildsafari.ui.components.BottomNavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Notifications") }) },
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        Text(
            text = "No new notifications 🎉",
            modifier = androidx.compose.ui.Modifier.padding(innerPadding).padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


