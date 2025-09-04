package com.example.wildsafari.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.wildsafari.Animal
import com.example.wildsafari.R
import com.example.wildsafari.ROUTE_ADDANIMAL
import com.example.wildsafari.ROUTE_ANIMALS
import com.example.wildsafari.ROUTE_HOME

import com.example.wildsafari.viewmodel.AnimalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAnimalScreen(navController: NavHostController) {
    val context = LocalContext.current
    val animalViewModel = AnimalViewModel(navController, context)

    val navItems = listOf("Home", "Add Animal", "Animals", "Notifications")
    val navIcons = listOf(Icons.Default.Home, Icons.Default.Pets, Icons.Default.Menu, Icons.Default.Notifications)
    val navRoutes = listOf(ROUTE_HOME, ROUTE_ADDANIMAL, ROUTE_ANIMALS, ROUTE_HOME)

    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Animal") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4CAF50),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Search, contentDescription = "Search") }
                    IconButton(onClick = {}) { Icon(Icons.Default.Settings, contentDescription = "Settings") }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF4CAF50), contentColor = Color.White) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(navIcons[index], contentDescription = item) },
                        label = { Text(item, color = Color.White) },
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                            navController.navigate(navRoutes[index]) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // ✅ Scrollable content
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState()), // <-- makes the form scrollable
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // --- Form State ---
            var name by remember { mutableStateOf("") }
            var species by remember { mutableStateOf("") }
            var description by remember { mutableStateOf("") }
            var habitat by remember { mutableStateOf("") }
            var price by remember { mutableStateOf("") }
            var imageUri by remember { mutableStateOf<Uri?>(null) }

            // Image Picker
            val imagePickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? -> imageUri = uri }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Add New Animal",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Image Preview + Picker
            Card(
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier
                    .size(140.dp)
                    .clickable { imagePickerLauncher.launch("image/*") }
                    .shadow(8.dp, CircleShape)
            ) {
                AsyncImage(
                    model = imageUri ?: R.drawable.elephant,
                    contentDescription = "Animal Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(140.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text("Choose Animal Image")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Input Fields ---
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Animal Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = species,
                onValueChange = { species = it },
                label = { Text("Species") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = habitat,
                onValueChange = { habitat = it },
                label = { Text("Habitat") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- Submit Button ---
            Button(
                onClick = {
                    animalViewModel.uploadAnimal(
                        name = name,
                        species = species,
                        description = description,
                        habitat = habitat,
                        price = price.toDoubleOrNull() ?: 0.0,
                        imageUri = imageUri
                    )
                    // Clear form
                    name = ""
                    species = ""
                    description = ""
                    habitat = ""
                    price = ""
                    imageUri = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text("Add Animal")
            }

            Spacer(modifier = Modifier.height(40.dp)) // extra space at bottom
        }
    }
}
