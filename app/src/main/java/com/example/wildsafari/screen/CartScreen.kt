package com.example.wildsafari.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.wildsafari.BookingItem
import com.example.wildsafari.ROUTE_HOME
import com.example.wildsafari.viewmodel.BookingViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@Composable
fun CartScreen(
    navController: NavController,
    bookingViewModel: BookingViewModel = viewModel()
) {
    val cartItems = bookingViewModel.cartItems.value
    val totalPrice = bookingViewModel.totalPrice.value
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(
        bottomBar = { BottomNavBar(navController) } // Replace with your actual BottomNavBar
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Your Cart", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Your cart is empty")
                }
            } else {
                // Scrollable list of cart items
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemRow(item = item, onRemove = { bookingViewModel.removeFromCart(item) })
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Total: $${totalPrice}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (currentUser != null) {
                            val bookingData = hashMapOf(
                                "id" to UUID.randomUUID().toString(),
                                "userId" to currentUser.uid,
                                "items" to cartItems.map { mapOf(
                                    "name" to it.name,
                                    "type" to it.type,
                                    "price" to it.price,
                                    "date" to it.date,
                                    "quantity" to it.quantity
                                )},
                                "totalPrice" to totalPrice,
                                "timestamp" to System.currentTimeMillis()
                            )

                            firestore.collection("bookings")
                                .add(bookingData)
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Proceed to Checkout")
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: BookingItem, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(item.name, style = MaterialTheme.typography.bodyLarge)
                Text("Price: $${item.price}", style = MaterialTheme.typography.bodyMedium)
                if (item.date.isNotEmpty()) {
                    Text("Date: ${item.date}", style = MaterialTheme.typography.bodySmall)
                }
            }
            Button(onClick = onRemove) {
                Text("Remove")
            }
        }
    }
}
