package com.example.unieats.frontend.dashboard.admin.OrderManagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.backend.dbData.Order
import com.example.unieats.backend.repository.OrderRepository

class OrderManagementViewModel : ViewModel() {
    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> get() = _orders

    private var initialized = false

    fun init(repo: OrderRepository) {
        if (initialized) return
        initialized = true

        repo.observeAllOrders()
        repo.orderLiveData.observeForever {
            _orders.postValue(it)
        }

        // Optional: store repo reference only if you need to call clearListeners()
    }

}



