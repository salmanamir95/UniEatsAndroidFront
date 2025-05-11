package com.example.unieats.backend.dbData

enum class OrderStatus {
    PENDING,
    PREPARING,
    SERVED,
    CANCELLED
}


data class Order(
    val orderId: String = "",
    val userId: String = "",
    val userName: String = "",
    val items: List<OrderItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val status: OrderStatus = OrderStatus.PENDING,
    val timestamp: Long = System.currentTimeMillis()
)