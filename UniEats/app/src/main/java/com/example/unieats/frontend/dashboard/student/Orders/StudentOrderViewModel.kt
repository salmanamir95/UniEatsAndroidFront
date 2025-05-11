package com.example.unieats.frontend.dashboard.student.Orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unieats.backend.dbData.Order
import com.example.unieats.backend.repository.OrderRepository
import com.example.unieats.frontend.dashboard.student.SharedViewModels.SharedStudentViewModel
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class StudentOrderViewModel(private val sharedStudentViewModel: SharedStudentViewModel) : ViewModel() {

    private val repository = OrderRepository()

    // 👤 Student's order history
    private val _orderHistory = MutableLiveData<List<Order>>()
    val orderHistory: LiveData<List<Order>> = _orderHistory

    // 📡 Real-time updates for ongoing orders
    private val _ongoingOrders = MutableLiveData<List<Order>>()
    val ongoingOrders: LiveData<List<Order>> = _ongoingOrders

    // 📆 Today’s orders by student
    private val _todaysOrders = MutableLiveData<List<Order>>()
    val todaysOrders: LiveData<List<Order>> = _todaysOrders

    // 📅 This month’s orders by student
    private val _monthlyOrders = MutableLiveData<List<Order>>()
    val monthlyOrders: LiveData<List<Order>> = _monthlyOrders

    private val activeListeners = mutableListOf<ValueEventListener>()

    // 🔁 Observe all past orders (history)
    fun observeOrderHistory() {
        val student = sharedStudentViewModel.getStudent()
        student?.let {
            val listener = repository.observeUserOrders(it.id) {
                _orderHistory.postValue(it.sortedByDescending { o -> o.timestamp })
            }
            activeListeners.add(listener)
        }
    }

    // 🔁 Real-time tracking of active (not served/cancelled) orders
    fun observeOngoingOrders() {
        val student = sharedStudentViewModel.getStudent()
        student?.let {
            val listener = repository.observeUserOrders(it.id) { all ->
                val ongoing = all.filter {
                    it.status.name != "SERVED" && it.status.name != "CANCELLED"
                }
                _ongoingOrders.postValue(ongoing)
            }
            activeListeners.add(listener)
        }
    }

    // 🔁 Filter only today's orders
    fun observeTodaysOrders() {
        val student = sharedStudentViewModel.getStudent()
        student?.let {
            val listener = repository.observeUserOrders(it.id) { all ->
                val todayStart = repository.getStartOfDayTimestamp()
                val todayOrders = all.filter { it.timestamp >= todayStart }
                _todaysOrders.postValue(todayOrders)
            }
            activeListeners.add(listener)
        }
    }

    // 🔁 Filter only this month’s orders
    fun observeMonthlyOrders() {
        val student = sharedStudentViewModel.getStudent()
        student?.let {
            val listener = repository.observeUserOrders(it.id) { all ->
                val monthStart = repository.getStartOfMonthTimestamp()
                val monthly = all.filter { it.timestamp >= monthStart }
                _monthlyOrders.postValue(monthly)
            }
            activeListeners.add(listener)
        }
    }

    override fun onCleared() {
        super.onCleared()
        activeListeners.forEach { repository.removeListener(it) }
        activeListeners.clear()
    }
}
