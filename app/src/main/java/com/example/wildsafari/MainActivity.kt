package com.example.wildsafari

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wildsafari.ui.navigation.AppNavHost
import com.example.wildsafari.ui.theme.WildSafariTheme
import com.example.wildsafari.viewmodel.BookingViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            WildSafariTheme {
                // ✅ Create ONE instance of BookingViewModel
                val bookingViewModel: BookingViewModel = viewModel()

                // ✅ Pass it into AppNavHost
                AppNavHost(bookingViewModel = bookingViewModel)
            }
        }
    }
}
