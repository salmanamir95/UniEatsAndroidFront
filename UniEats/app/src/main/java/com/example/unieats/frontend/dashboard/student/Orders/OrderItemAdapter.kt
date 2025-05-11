package com.example.unieats.frontend.dashboard.student.Orders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.unieats.backend.dbData.OrderItem
import com.example.unieats.R

class OrderItemAdapter(private val context: Context, private val items: List<OrderItem>) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)

        val item = items[position]

        val itemNameTextView: TextView = view.findViewById(R.id.itemName)
        val itemQuantityTextView: TextView = view.findViewById(R.id.itemQuantity)

        itemNameTextView.text = item.name
        itemQuantityTextView.text = "Quantity: ${item.quantity}"

        return view
    }
}
