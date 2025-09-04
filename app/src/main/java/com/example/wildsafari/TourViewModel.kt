package com.example.wildsafari

data class Tour(
    val id: String = "",
    val name: String = "",
    val durationDays: Int = 0,
    val priceRange: String = "",
    val difficulty: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val price: Double = 0.0
)

