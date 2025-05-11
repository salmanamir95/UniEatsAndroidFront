package com.example.unieats.frontend.dashboard.student.MenuItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.backend.dbData.MenuItemReview
import com.example.unieats.backend.repository.ReviewRepository

class ReviewViewModel : ViewModel() {

    private val repository = ReviewRepository()

    private val _reviews = MutableLiveData<List<MenuItemReviewModel>>()
    val reviews: LiveData<List<MenuItemReviewModel>> get() = _reviews

    fun loadReviews(menuItemId: String) {
        repository.getReviewsForMenuItem(menuItemId)
        repository.reviewsLiveData.observeForever {
            _reviews.postValue(it)
        }
    }

    fun submitReview(
        menuItemId: String,
        userId: String,
        userName: String,
        rating: Float,
        comment: String,
        callback: (Boolean) -> Unit
    ) {
        val review = MenuItemReview(
            reviewId = "", // Will be set by Firebase push().key
            userId = userId,
            userName = userName,
            rating = rating,
            comment = comment,
            timestamp = System.currentTimeMillis()
        )
        repository.addReview(menuItemId, review, callback)
    }
}
