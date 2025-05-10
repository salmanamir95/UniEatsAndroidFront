package com.example.unieats.backend.dbData

import com.example.unieats.frontend.dashboard.student.MenuItem.MenuItemReviewModel

data class MenuItemReview(
    val reviewId: String = "",
    val menuItemId: String = "",
    val userId: String = "",
    val userName: String = "",
    val rating: Float = 0f,
    val comment: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toModel(): MenuItemReviewModel {
        return MenuItemReviewModel(
            userName = userName,
            rating = rating,
            comment = comment,
            timestamp = timestamp
        )
    }
}
