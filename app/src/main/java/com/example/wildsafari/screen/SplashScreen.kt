package com.example.wildsafari.screen



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.wildsafari.R
import com.example.wildsafari.ROUTE_LOGIN
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    // Navigate to Login Screen after 3 seconds
    LaunchedEffect(key1 = true) {
        delay(3000) // 3 seconds delay
        navController.navigate(ROUTE_LOGIN) {
            popUpTo(ROUTE_LOGIN) { inclusive = true }
        }
    }

    // UI of the Splash Screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2E7D32)), // Forest green background
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Splash Image
            Image(
                painter = painterResource(R.drawable.wildlife), // your wildlife.webp in drawable
                contentDescription = "Wild Safari Logo",
                modifier = Modifier
                    .size(200.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Welcome Text
            Text(
                text = "Welcome to Wild Safari",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Explore the wild like never before!",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}
