package com.example.wildsafari

data class BookingItem(
    val id: String = "",
    val type: String = "", // "animal", "tour", or "place"
    val name: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1,
    val date: String = "" // Booking date
)