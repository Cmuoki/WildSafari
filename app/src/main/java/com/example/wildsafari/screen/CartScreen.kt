package com.example.wildsafari.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.wildsafari.BookingItem
import com.example.wildsafari.ROUTE_HOME
import com.example.wildsafari.viewmodel.BookingViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

@Composable
fun CartScreen(navController: NavController, bookingViewModel: BookingViewModel = viewModel()) {
    val cartItems = bookingViewModel.cartItems.value
    val totalPrice = bookingViewModel.totalPrice.value
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser
    val database = FirebaseDatabase.getInstance().reference

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color(0xFFF1F8E9) // light nature-inspired background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                "Your Booking",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color(0xFF2E7D32))
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Your cart is empty", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems) { item ->
                        BookingCard(item) {
                            bookingViewModel.removeFromCart(item)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Total: $${totalPrice}",
                            style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF1B5E20))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (currentUser != null) {
                            val bookingId = UUID.randomUUID().toString()
                            val bookingData = mapOf(
                                "bookingId" to bookingId,
                                "userId" to currentUser.uid,
                                "items" to cartItems.map { item ->
                                    mapOf(
                                        "id" to item.id,
                                        "name" to item.name,
                                        "type" to item.type,
                                        "price" to item.price,
                                        "quantity" to item.quantity,
                                        "date" to item.date
                                    )
                                },
                                "totalPrice" to totalPrice,
                                "timestamp" to System.currentTimeMillis()
                            )

                            database.child("bookings").child(bookingId).setValue(bookingData)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Booking confirmed!", Toast.LENGTH_LONG).show()
                                    bookingViewModel.clearCart()
                                    navController.navigate(ROUTE_HOME) {
                                        popUpTo(ROUTE_HOME) { inclusive = true }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        } else {
                            Toast.makeText(context, "You must be logged in to book.", Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Confirm Booking", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun BookingCard(item: BookingItem, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9)), // light green card
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(item.name, style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF2E7D32)))
                Text("Price: $${item.price}", style = MaterialTheme.typography.bodyMedium)
                if (item.date.isNotEmpty()) {
                    Text("Date: ${item.date}", style = MaterialTheme.typography.bodySmall)
                }
            }
            Button(
                onClick = onRemove,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Text("Remove", color = Color.White)
            }
        }
    }
}
