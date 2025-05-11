package com.example.unieats.backend.dbData

data class OrderItem(
    val itemId: String,  // Unique ID for the menu item
    val name: String,    // Name of the menu item
    val quantity: Int,   // Quantity of the item ordered
    val price: Double,   // Price of the menu item
    val totalPrice: Double // Total price (price * quantity)
)

