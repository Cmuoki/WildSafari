package com.example.wildsafari.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.wildsafari.BookingItem
import com.example.wildsafari.viewmodel.BookingViewModel
import java.util.*

@Composable
fun ToursScreen(navController: NavController, bookingViewModel: BookingViewModel = viewModel()) {
    val tours = listOf(
        BookingItem(id = "t1", type = "tour", name = "Safari Jeep Tour", price = 120.0),
        BookingItem(id = "t2", type = "tour", name = "Night Safari", price = 150.0),
        BookingItem(id = "t3", type = "tour", name = "River Cruise", price = 90.0)
    )

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Available Tours 🚌",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(tours) { tour ->
                    TourCard(tour, navController, bookingViewModel)
                }
            }
        }
    }
}

@Composable
fun TourCard(tour: BookingItem, navController: NavController, bookingViewModel: BookingViewModel) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            // Show DatePicker when tour is tapped
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                context,
                { _, year, month, day ->
                    val selectedDate = "$year-${month + 1}-$day"
                    val tourWithDate = tour.copy(date = selectedDate)
                    bookingViewModel.addToCart(tourWithDate)
                    navController.navigate("cart")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(tour.name, style = MaterialTheme.typography.bodyLarge)
            Text("Price: $${tour.price}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}



