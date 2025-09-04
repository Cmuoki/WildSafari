package com.example.wildsafari.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wildsafari.BookingItem
import com.example.wildsafari.ROUTE_CART

import com.example.wildsafari.viewmodel.BookingViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen(navController: NavController, bookingViewModel: BookingViewModel) {
    val places = listOf(
        BookingItem(id = "p1", type = "place", name = "Savannah Plains", price = 70.0),
        BookingItem(id = "p2", type = "place", name = "Elephant Valley", price = 80.0),
        BookingItem(id = "p3", type = "place", name = "Rhino Sanctuary", price = 100.0)
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Places to Visit 🏞️") }) },
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(places) { place ->
                    PlaceCard(place, navController, bookingViewModel)
                }
            }
        }
    }
}

@Composable
fun PlaceCard(place: BookingItem, navController: NavController, bookingViewModel: BookingViewModel) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            // When tapped, ask the tourist to pick a date
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$year-${month + 1}-$dayOfMonth"
                    val placeWithDate = place.copy(date = selectedDate)
                    bookingViewModel.addToCart(placeWithDate)
                    navController.navigate(ROUTE_CART)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(place.name, style = MaterialTheme.typography.bodyLarge)
            Text("Price: $${place.price}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}




