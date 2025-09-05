package com.example.wildsafari.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.wildsafari.BookingItem
import com.example.wildsafari.ROUTE_CART
import com.example.wildsafari.ui.components.BottomNavBar
import com.example.wildsafari.viewmodel.BookingViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToursScreen(navController: NavController, bookingViewModel: BookingViewModel = viewModel()) {
    val tours = listOf(
        BookingItem(id = "t1", type = "tour", name = "Safari Jeep Tour", price = 120.0),
        BookingItem(id = "t2", type = "tour", name = "Night Safari", price = 150.0),
        BookingItem(id = "t3", type = "tour", name = "River Cruise", price = 90.0)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Available Tours 🚌") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF2E7D32),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color(0xFFE8F5E9) // light green nature-inspired background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tours) { tour ->
                TourCard(tour, navController, bookingViewModel)
            }
        }
    }
}

@Composable
fun TourCard(tour: BookingItem, navController: NavController, bookingViewModel: BookingViewModel) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                context,
                { _, year, month, day ->
                    val selectedDate = "$year-${month + 1}-$day"
                    val tourWithDate = tour.copy(date = selectedDate)
                    bookingViewModel.addToCart(tourWithDate)
                    navController.navigate(ROUTE_CART)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Map,
                contentDescription = tour.name,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    tour.name,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF1B5E20))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Price: $${tour.price}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
            }
        }
    }
}



