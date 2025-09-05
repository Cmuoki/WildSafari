package com.example.wildsafari.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wildsafari.BookingItem
import com.example.wildsafari.ROUTE_CART
import com.example.wildsafari.ui.components.BottomNavBar
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
        topBar = {
            TopAppBar(
                title = { Text("Places to Visit 🏞️") },
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
            items(places) { place ->
                PlaceCard(place, navController, bookingViewModel)
            }
        }
    }
}

@Composable
fun PlaceCard(place: BookingItem, navController: NavController, bookingViewModel: BookingViewModel) {
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
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = place.name,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    place.name,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF1B5E20))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Price: $${place.price}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
            }
        }
    }
}





