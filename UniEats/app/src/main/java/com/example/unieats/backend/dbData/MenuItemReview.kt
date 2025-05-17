package com.example.unieats.backend.dbData


data class MenuItemReview(
    val reviewId: String = "",
    val menuItemId: String = "",
    val userId: String = "",
    val userName: String = "",
    val rating: Float = 0f,
    val comment: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {

}
