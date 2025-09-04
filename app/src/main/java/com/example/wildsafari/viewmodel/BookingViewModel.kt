package com.example.wildsafari.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.wildsafari.BookingItem

class BookingViewModel : ViewModel() {
    val cartItems = mutableStateOf<List<BookingItem>>(emptyList())
    val totalPrice = mutableStateOf(0.0)

    // Add item to cart
    fun addToCart(item: BookingItem) {
        val updatedCart = cartItems.value.toMutableList()
        updatedCart.add(item)
        cartItems.value = updatedCart
        recalculateTotal()
    }

    // Remove item from cart
    fun removeFromCart(item: BookingItem) {
        val updatedCart = cartItems.value.toMutableList()
        updatedCart.remove(item)
        cartItems.value = updatedCart
        recalculateTotal()
    }

    // Update item (for changing date, quantity, etc.)
    fun updateCartItem(updatedItem: BookingItem) {
        val updatedCart = cartItems.value.toMutableList()
        val index = updatedCart.indexOfFirst { it.id == updatedItem.id }
        if (index != -1) {
            updatedCart[index] = updatedItem
            cartItems.value = updatedCart
            recalculateTotal()
        }
    }

    // Clear all items
    fun clearCart() {
        cartItems.value = emptyList()
        totalPrice.value = 0.0
    }

    // Recalculate total price
    private fun recalculateTotal() {
        totalPrice.value = cartItems.value.sumOf { it.price * it.quantity }
    }
}


