package com.example.unieats.backend.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.unieats.backend.dbData.MenuItemReview
import com.example.unieats.frontend.dashboard.student.MenuItem.MenuItemReviewModel
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ReviewRepository {

    private val database = FirebaseDatabase.getInstance()
    private val reviewRef = database.getReference("menu_reviews")
    private val activeListeners = mutableMapOf<String, ValueEventListener>()

    private val _reviewsLiveData = MutableLiveData<List<MenuItemReviewModel>>()
    val reviewsLiveData: LiveData<List<MenuItemReviewModel>> get() = _reviewsLiveData

    // CREATE: Add a new review to a specific menu item
    suspend fun addReview(menuItemId: String, review: MenuItemReview): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val reviewId = reviewRef.child(menuItemId).push().key ?: return@withContext false
                val reviewWithId = review.copy(reviewId = reviewId)
                reviewRef.child(menuItemId).child(reviewId).setValue(reviewWithId).await()
                true
            } catch (e: Exception) {
                Log.e("ReviewRepository", "Error adding review: ${e.message}")
                false
            }
        }
    }

    // READ: Get all reviews for a specific menu item (real-time updates)
    fun getReviewsForMenuItem(menuItemId: String) {
        val query = reviewRef.child(menuItemId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reviews = snapshot.children.mapNotNull { child ->
                    child.getValue(MenuItemReview::class.java)?.toModel()
                }
                _reviewsLiveData.value = reviews
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ReviewRepository", "Listener cancelled: ${error.message}")
                _reviewsLiveData.value = emptyList()
            }
        }

        // Remove existing listener if present and add new one
        activeListeners[menuItemId]?.let { query.removeEventListener(it) }
        activeListeners[menuItemId] = listener
        query.addValueEventListener(listener)
    }

    // UPDATE: Update a review for a specific menu item
    suspend fun updateReview(
        menuItemId: String,
        reviewId: String,
        updatedReview: MenuItemReview
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                reviewRef.child(menuItemId).child(reviewId).setValue(updatedReview).await()
                true
            } catch (e: Exception) {
                Log.e("ReviewRepository", "Error updating review: ${e.message}")
                false
            }
        }
    }

    // DELETE: Delete a review for a specific menu item
    suspend fun deleteReview(menuItemId: String, reviewId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                reviewRef.child(menuItemId).child(reviewId).removeValue().await()
                true
            } catch (e: Exception) {
                Log.e("ReviewRepository", "Error deleting review: ${e.message}")
                false
            }
        }
    }

    // Cleanup listeners when no longer needed
    fun removeListener(menuItemId: String) {
        activeListeners[menuItemId]?.let {
            reviewRef.child(menuItemId).removeEventListener(it)
            activeListeners.remove(menuItemId)
        }
    }
}