package com.example.wildsafari.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wildsafari.Tour
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TourViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _tours = MutableStateFlow<List<Tour>>(emptyList())
    val tours: StateFlow<List<Tour>> = _tours

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadTours() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val snapshot = db.collection("tours").get().await()
                val toursList = snapshot.documents.mapNotNull { document ->
                    try {
                        Tour(
                            id = document.id,
                            name = document.getString("name") ?: "",
                            durationDays = document.getLong("durationDays")?.toInt() ?: 0,
                            priceRange = document.getString("priceRange") ?: "",
                            difficulty = document.getString("difficulty") ?: "",
                            description = document.getString("description") ?: "",
                            imageUrl = document.getString("imageUrl") ?: "",
                            price = document.getDouble("price") ?: 0.0
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                _tours.value = toursList
            } catch (e: Exception) {
                _tours.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

