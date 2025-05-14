package com.example.unieats.frontend.dashboard.admin.OrderManagement

import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unieats.backend.dbData.Order
import com.example.unieats.backend.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderManagementViewModel : ViewModel() {
    lateinit var repository: OrderRepository
    lateinit var _orders: MutableLiveData<List<Order>>

    fun isInitialized(): Boolean = this::repository.isInitialized

    fun init(repo : OrderRepository){
        if(!this.isInitialized()){
            repository = repo
            repo.orderLiveData.observeForever{
                _orders.postValue(it)
            }
        }
    }

    fun loadOrderItems(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.observeAllOrders()
        }
    }

}
