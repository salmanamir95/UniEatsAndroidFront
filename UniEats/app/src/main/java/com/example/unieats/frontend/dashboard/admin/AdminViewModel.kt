package com.example.unieats.frontend.dashboard.admin

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.unieats.backend.repository.MenuRepository
import com.example.unieats.backend.repository.OrderRepository
import com.example.unieats.backend.repository.ReviewRepository
import com.example.unieats.backend.repository.TableRepository
import com.example.unieats.backend.repository.UserRepository
import com.example.unieats.frontend.MainActivity

class AdminViewModel(private val context: Context) : ViewModel() {
    val orderRepository = OrderRepository()
    val reviewRepository = ReviewRepository()
    val tableRepository = TableRepository()
    val userRepository = UserRepository()
}
