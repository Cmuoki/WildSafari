package com.example.wildsafari.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wildsafari.Animal
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SafariViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _animals = MutableStateFlow<List<Animal>>(emptyList())
    val animals: StateFlow<List<Animal>> = _animals

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadAnimals() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val snapshot = db.collection("animals").get().await()
                val animalsList = snapshot.documents.mapNotNull { document ->
                    try {
                        Animal(
                            id = document.id,
                            name = document.getString("name") ?: "",
                            species = document.getString("species") ?: "",
                            description = document.getString("description") ?: "",
                            habitat = document.getString("habitat") ?: "",
                            imageUrl = document.getString("imageUrl") ?: "",
                            price = document.getDouble("price") ?: 0.0
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                _animals.value = animalsList
            } catch (e: Exception) {
                // Handle error - you might want to show a message
                _animals.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}