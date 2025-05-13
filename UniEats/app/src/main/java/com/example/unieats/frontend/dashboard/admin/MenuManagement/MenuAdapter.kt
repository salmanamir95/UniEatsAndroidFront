package com.example.unieats.frontend.dashboard.admin.MenuManagement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.unieats.databinding.ItemMenuBinding
import com.example.unieats.frontend.dashboard.student.Menu.MenuItemModel

class MenuAdapter : ListAdapter<MenuItemModel, MenuAdapter.MenuViewHolder>(MenuDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = getItem(position)
        holder.bind(menuItem)
    }

    inner class MenuViewHolder(private val binding: ItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: MenuItemModel) {
            binding.menuItemName.text = menuItem.name
            // Set other views like price, image, etc.
        }
    }

    class MenuDiffCallback : DiffUtil.ItemCallback<MenuItemModel>() {
        override fun areItemsTheSame(oldItem: MenuItemModel, newItem: MenuItemModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MenuItemModel, newItem: MenuItemModel): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.name == newItem.name &&
                    oldItem.category == newItem.category &&
                    oldItem.price == newItem.price &&
                    oldItem.quantity == newItem.quantity &&
                    oldItem.isSelected == newItem.isSelected
            // Avoid comparing imageBitmap unless needed
        }

    }
}