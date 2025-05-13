package com.example.unieats.frontend.dashboard.admin.MenuManagement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unieats.databinding.ItemMenuBinding
import com.example.unieats.frontend.dashboard.student.Menu.MenuItemModel

class MenuAdapter(private val onItemClick: (MenuItemModel) -> Unit) :
    ListAdapter<MenuItemModel, MenuAdapter.MenuViewHolder>(MenuDiffCallback()) {

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
            binding.menuItemPrice.text = "$${menuItem.price}"
            Glide.with(binding.root.context)
                .load(menuItem.imageBitmap)
                .into(binding.menuItemImage)

            // Detect the item click
            binding.root.setOnClickListener {
                onItemClick(menuItem)
            }
        }
    }

    class MenuDiffCallback : DiffUtil.ItemCallback<MenuItemModel>() {
        override fun areItemsTheSame(oldItem: MenuItemModel, newItem: MenuItemModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MenuItemModel, newItem: MenuItemModel): Boolean {
            return oldItem == newItem
        }
    }
}
