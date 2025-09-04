package com.example.wildsafari.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wildsafari.ROUTE_ADDANIMAL
import com.example.wildsafari.ROUTE_ANIMALS
import com.example.wildsafari.ROUTE_CART
import com.example.wildsafari.ROUTE_HOME
import com.example.wildsafari.ROUTE_PLACES
import com.example.wildsafari.ROUTE_TOURS

import com.example.wildsafari.viewmodel.BookingViewModel

@Composable
fun HomeScreen(navController: NavController, bookingViewModel: BookingViewModel) {
    val cartItems = bookingViewModel.cartItems.value
    val totalPrice = bookingViewModel.totalPrice.value

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Wild Safari 🐘",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                // Cart Icon with badge
                Box {
                    IconButton(onClick = { navController.navigate(ROUTE_CART) }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                    if (cartItems.isNotEmpty()) {
                        Badge(modifier = Modifier.align(Alignment.TopEnd)) {
                            Text(text = cartItems.size.toString())
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Book your safari adventure!",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Quick Access Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(quickAccessItems) { item ->
                    QuickAccessCard(item = item, navController = navController, bookingViewModel = bookingViewModel)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Cart Summary
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🛒 ${cartItems.size} items in cart",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Total: $${totalPrice}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

// ✅ Quick Access Item Data Class
data class QuickAccessItem(
    val title: String,
    val icon: @Composable () -> Unit,
    val route: String
)

val quickAccessItems = listOf(
    QuickAccessItem("Animals", { Icon(Icons.Default.Pets, contentDescription = "Animals") }, ROUTE_ANIMALS),
    QuickAccessItem("Places", { Icon(Icons.Default.Explore, contentDescription = "Places") }, ROUTE_PLACES),
    QuickAccessItem("Tours", { Icon(Icons.Default.Tour, contentDescription = "Tours") }, ROUTE_TOURS),
    QuickAccessItem("Cart", { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") }, ROUTE_CART)
)

// ✅ Quick Access Card
@Composable
fun QuickAccessCard(item: QuickAccessItem, navController: NavController, bookingViewModel: BookingViewModel) {
    Card(
        modifier = Modifier.size(120.dp, 100.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = { navController.navigate(item.route) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item.icon()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

// ✅ Updated Bottom Navigation Bar
@Composable
fun BottomNavBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = { navController.navigate(ROUTE_HOME) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Add, contentDescription = "Add Animal") },
            label = { Text("Add Animal") },
            selected = false,
            onClick = { navController.navigate(ROUTE_ADDANIMAL) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Pets, contentDescription = "Animals") },
            label = { Text("Animals") },
            selected = false,
            onClick = { navController.navigate(ROUTE_ANIMALS) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Notifications") },
            label = { Text("Notifications") },
            selected = false,
            onClick = { /* TODO: Notifications screen */ }
        )
    }
}
