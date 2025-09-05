package com.example.wildsafari.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wildsafari.BookingItem
import com.example.wildsafari.ROUTE_CART
import com.example.wildsafari.R
import com.example.wildsafari.ui.components.BottomNavBar
import com.example.wildsafari.viewmodel.BookingViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen(navController: NavController, bookingViewModel: BookingViewModel) {
    val places = listOf(
        BookingItem(id = "p1", type = "place", name = "Savannah Plains", price = 70.0, imageRes = R.drawable.savannah),
        BookingItem(id = "p2", type = "place", name = "Elephant Valley", price = 80.0, imageRes = R.drawable.elephantvalley),
        BookingItem(id = "p3", type = "place", name = "Rhino Sanctuary", price = 100.0, imageRes = R.drawable.rhino)
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
        containerColor = Color(0xFFE8F5E9)
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp),
        onClick = {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                context,
                { _, year, month, day ->
                    val selectedDate = "$year-${month + 1}-$day"
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
            if (place.imageRes != 0) {
                Image(
                    painter = painterResource(id = place.imageRes),
                    contentDescription = place.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
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
