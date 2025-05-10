package com.example.unieats.backend.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.unieats.backend.dbData.MenuItemReview
import com.example.unieats.frontend.dashboard.student.MenuItem.MenuItemReviewModel
import com.google.firebase.database.FirebaseDatabase

class ReviewRepository {

    private val database = FirebaseDatabase.getInstance()
    private val reviewRef = database.getReference("menu_reviews") // Root node: "menu_reviews/menuItemId/reviewId"

    // LiveData for reviews per menu item
    private val _reviewsLiveData = MutableLiveData<List<MenuItemReviewModel>>()
    val reviewsLiveData: LiveData<List<MenuItemReviewModel>> get() = _reviewsLiveData

    // CREATE: Add a new review to a specific menu item
    fun addReview(menuItemId: String, review: MenuItemReview, callback: (Boolean) -> Unit) {
        val reviewId = reviewRef.child(menuItemId).push().key
        reviewId?.let {
            val reviewWithId = review.copy(reviewId = it)
            reviewRef.child(menuItemId).child(it).setValue(reviewWithId).addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
        } ?: callback(false)
    }

    // READ: Get all reviews for a specific menu item
    fun getReviewsForMenuItem(menuItemId: String) {
        reviewRef.child(menuItemId).get().addOnSuccessListener { snapshot ->
            val reviews = mutableListOf<MenuItemReviewModel>()
            for (child in snapshot.children) {
                val review = child.getValue(MenuItemReview::class.java)
                if (review != null) {
                    reviews.add(review.toModel())
                }
            }
            _reviewsLiveData.postValue(reviews)
        }.addOnFailureListener {
            _reviewsLiveData.postValue(emptyList())
        }
    }

    // UPDATE: Update a review for a specific menu item
    fun updateReview(menuItemId: String, reviewId: String, updatedReview: MenuItemReview, callback: (Boolean) -> Unit) {
        reviewRef.child(menuItemId).child(reviewId).setValue(updatedReview).addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }
    }

    // DELETE: Delete a review for a specific menu item
    fun deleteReview(menuItemId: String, reviewId: String, callback: (Boolean) -> Unit) {
        reviewRef.child(menuItemId).child(reviewId).removeValue().addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }
    }
}
