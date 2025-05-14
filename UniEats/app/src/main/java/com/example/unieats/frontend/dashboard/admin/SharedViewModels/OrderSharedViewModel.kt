package com.example.unieats.frontend.dashboard.admin.SharedViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.backend.repository.OrderRepository

class OrderSharedViewModel: ViewModel() {
    public var data = MutableLiveData<OrderRepository>()
}