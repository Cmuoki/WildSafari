package com.example.wildsafari.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wildsafari.BookingItem
import com.example.wildsafari.ROUTE_CART
import com.example.wildsafari.ui.screens.BottomNavBar
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
        topBar = { TopAppBar(title = { Text("Animals 🦁") }) },
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(animals) { animal ->
                    AnimalCard(animal, navController, bookingViewModel)
                }
            }
        }
    }
}

@Composable
fun AnimalCard(animal: BookingItem, navController: NavController, bookingViewModel: BookingViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            bookingViewModel.addToCart(animal)
            navController.navigate(ROUTE_CART)
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(animal.name, style = MaterialTheme.typography.bodyLarge)
            Text("Price: $${animal.price}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

