package com.example.unieats.frontend.dashboard.admin.OrderManagement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.unieats.backend.dbData.Order
import com.example.unieats.databinding.ItemOrderBinding

class OrderManagementAdapter :
    ListAdapter<Order, OrderManagementAdapter.OrderViewHolder>(OrderDiffCallback()) {

}

class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean =
        oldItem.orderId == newItem.orderId

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean =
        oldItem == newItem
}
