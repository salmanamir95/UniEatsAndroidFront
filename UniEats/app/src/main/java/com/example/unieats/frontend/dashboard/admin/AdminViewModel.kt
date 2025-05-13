package com.example.unieats.frontend.dashboard.admin

import androidx.lifecycle.ViewModel
import com.example.unieats.backend.repository.MenuRepository
import com.example.unieats.backend.repository.OrderRepository
import com.example.unieats.backend.repository.ReviewRepository
import com.example.unieats.backend.repository.TableRepository
import com.example.unieats.backend.repository.UserRepository

class AdminViewModel : ViewModel() {

    val menuRepository = MenuRepository()
    val orderRepository = OrderRepository()
    val reviewRepository = ReviewRepository()
    val tableRepository = TableRepository()
    val userRepository = UserRepository()
}
