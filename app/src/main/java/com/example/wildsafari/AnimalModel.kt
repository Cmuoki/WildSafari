package com.example.wildsafari

data class Animal(
    val id: String = "", // Change from Int? to String for Firestore
    val name: String = "",
    val species: String = "",
    val description: String = "",
    val habitat: String = "",
    val imageUrl: String = "", // Change from image_url to imageUrl
    val price: Double = 0.0
)