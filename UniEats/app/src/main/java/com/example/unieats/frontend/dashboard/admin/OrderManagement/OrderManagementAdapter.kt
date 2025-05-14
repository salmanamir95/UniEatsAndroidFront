package com.example.unieats.frontend.dashboard.admin.OrderManagement
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.unieats.backend.dbData.Order
import com.example.unieats.backend.dbData.OrderItem
import com.example.unieats.databinding.ItemOrderBinding

class OrderManagementAdapter (private val OnItemClick: ((OrderItem) -> Unit)? = null) :
    ListAdapter<Order, OrderManagementAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderManagementAdapter.OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: OrderManagementAdapter.OrderViewHolder,
        position: Int
    ) {
        val orderItem = getItem(position)
        return holder.bind(orderItem)
    }

    inner class OrderViewHolder(private val binding: ItemOrderBinding)
        :androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)
    {
        fun bind(orderItem : Order){
            binding.orderId.text= orderItem.orderId
            binding.userName.text = orderItem.userName
            binding.totalAmount.text = orderItem.totalAmount.toString()
            binding.orderItems.text = orderItem.items.joinToString("\n") { "${it.name} x${it.quantity}" }
            binding.orderTime.text= orderItem.timestamp.toString()
        }
    }
}

class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean =
        oldItem.orderId == newItem.orderId

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean =
        oldItem == newItem
}
