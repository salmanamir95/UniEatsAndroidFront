package com.example.unieats.frontend.dashboard.student.MenuItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unieats.R

class ReviewAdapter(private var reviews: List<MenuItemReviewModel>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        val ratingBar: RatingBar = itemView.findViewById(R.id.reviewRatingBar)
        val tvComment: TextView = itemView.findViewById(R.id.tvComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.tvUsername.text = review.userName
        holder.ratingBar.rating = review.rating
        holder.tvComment.text = review.comment
    }

    override fun getItemCount(): Int = reviews.size

    fun updateReviews(newReviews: List<MenuItemReviewModel>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}