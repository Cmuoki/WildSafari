//package com.example.wildsafari.ui.theme
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//
//class PlaceViewModel : ViewModel() {
//
//    private val db = FirebaseFirestore.getInstance()
//
//    private val _places = MutableStateFlow<List<Place>>(emptyList())
//    val places: StateFlow<List<Place>> = _places
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading
//
//    fun loadPlaces() {
//        _isLoading.value = true
//        viewModelScope.launch {
//            try {
//                val snapshot = db.collection("places").get().await()
//                val placesList = snapshot.documents.mapNotNull { document ->
//                    try {
//                        Place(
//                            id = document.id,
//                            name = document.getString("name") ?: "",
//                            country = document.getString("country") ?: "",
//                            description = document.getString("description") ?: "",
//                            bestTimeToVisit = document.getString("bestTimeToVisit") ?: "",
//                            price = document.getDouble("price") ?: 0.0
//                        )
//                    } catch (e: Exception) {
//                        null
//                    }
//                }
//                _places.value = placesList
//            } catch (e: Exception) {
//                _places.value = emptyList()
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//}

