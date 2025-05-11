package com.example.unieats.frontend.dashboard.student.Orders


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.backend.dbData.Order
import com.example.unieats.backend.dbData.OrderItem
import com.example.unieats.backend.repository.OrderRepository

class PlaceOrderViewModel : ViewModel() {

    private val repository = OrderRepository()

    // The current order to place
    private val _order = MutableLiveData<Order>()
    val order: LiveData<Order> = _order

    // Total bill amount
    private val _totalBill = MutableLiveData<Double>()
    val totalBill: LiveData<Double> = _totalBill

    // Payment method (Cash or Card)
    private val _paymentMethod = MutableLiveData<String>()
    val paymentMethod: LiveData<String> = _paymentMethod

    // Add item to the order
    fun addItemToOrder(item: OrderItem) {
        val currentOrder = _order.value ?: Order()
        _order.value = currentOrder.copy(
            items = currentOrder.items + item,
            totalAmount = currentOrder.totalAmount + item.totalPrice
        )
        _totalBill.value = _order.value?.totalAmount
    }

    // Set payment method
    fun setPaymentMethod(method: String) {
        _paymentMethod.value = method
    }

    // Place the order
    fun placeOrder() {
        _order.value?.let {
            repository.createOrder(it) { success ->
                if (success) {
                    // Handle successful order placement (e.g., navigate to confirmation screen)
                } else {
                    // Handle error
                }
            }
        }
    }

    // Set full list of items to the order (replaces previous ones)
    fun setOrderItems(items: List<OrderItem>) {
        val total = items.sumOf { it.totalPrice }
        _order.value = Order(items = items, totalAmount = total)
        _totalBill.value = total
    }

}
