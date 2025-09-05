package com.example.wildsafari.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wildsafari.BookingItem
import com.example.wildsafari.ROUTE_CART
import com.example.wildsafari.ui.components.BottomNavBar
import com.example.wildsafari.viewmodel.BookingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalListScreen(navController: NavController, bookingViewModel: BookingViewModel) {
    val animals = listOf(
        BookingItem(id = "a1", type = "animal", name = "Lion", price = 50.0),
        BookingItem(id = "a2", type = "animal", name = "Elephant", price = 75.0),
        BookingItem(id = "a3", type = "animal", name = "Giraffe", price = 60.0)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Animals 🦁") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF2E7D32), // forest green
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color(0xFFE8F5E9) // light green background for nature vibe
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(animals) { animal ->
                AnimalCard(animal, navController, bookingViewModel)
            }
        }
    }
}

@Composable
fun AnimalCard(animal: BookingItem, navController: NavController, bookingViewModel: BookingViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)), // white card
        onClick = {
            bookingViewModel.addToCart(animal)
            navController.navigate(ROUTE_CART)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Pets,
                contentDescription = animal.name,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    animal.name,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF1B5E20))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Price: $${animal.price}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
            }
        }
    }
}


