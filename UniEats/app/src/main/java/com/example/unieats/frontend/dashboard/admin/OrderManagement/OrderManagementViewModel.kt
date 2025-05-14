package com.example.unieats.frontend.dashboard.admin.OrderManagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.backend.dbData.Order
import com.example.unieats.backend.repository.OrderRepository

class OrderManagementViewModel(private val repository: OrderRepository) : ViewModel() {
    val orders: LiveData<List<Order>> = repository.orderLiveData

    fun loadOrders() {
        repository.observeAllOrders()
    }

    override fun onCleared() {
        super.onCleared()
        repository.clearListeners()
    }
}
