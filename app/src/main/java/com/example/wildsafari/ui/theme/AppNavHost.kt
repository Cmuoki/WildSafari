package com.example.wildsafari.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wildsafari.ROUTE_ADDANIMAL
import com.example.wildsafari.ROUTE_ANIMALS
import com.example.wildsafari.ROUTE_CART
import com.example.wildsafari.ROUTE_HOME
import com.example.wildsafari.ROUTE_LOGIN
import com.example.wildsafari.ROUTE_PLACES
import com.example.wildsafari.ROUTE_REGISTER
import com.example.wildsafari.ROUTE_SPLASH
import com.example.wildsafari.ROUTE_TOURS
import com.example.wildsafari.screen.AnimalListScreen
import com.example.wildsafari.screen.SplashScreen
import com.example.wildsafari.ui.screens.*
import com.example.wildsafari.viewmodel.BookingViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_SPLASH,
    bookingViewModel: BookingViewModel
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination
    ) {
        composable(ROUTE_HOME) {
            HomeScreen(navController, bookingViewModel)
        }
        composable(ROUTE_LOGIN) {
            LoginScreen(navController)
        }
        composable(ROUTE_REGISTER) {
            RegisterScreen(navController)
        }
        composable(ROUTE_ANIMALS) {
            AnimalListScreen(navController, bookingViewModel)
        }
        composable(ROUTE_ADDANIMAL) {
            AddAnimalScreen(navController)
        }
        composable(ROUTE_CART) {
            CartScreen(navController, bookingViewModel)
        }
        composable(ROUTE_PLACES) {
            PlacesScreen(navController, bookingViewModel)
        }
        composable(ROUTE_TOURS) {
            ToursScreen(navController, bookingViewModel)
        }
        composable(ROUTE_SPLASH) {
            SplashScreen(navController)
        }
    }
}






