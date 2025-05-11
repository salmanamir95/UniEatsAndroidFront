package com.example.unieats.frontend.dashboard.student.Menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unieats.R
import com.example.unieats.frontend.dashboard.student.SharedViewModels.SelectedMenuItemsSharedtoCart

class MenuAdapter(
private var items: List<MenuItemModel> = emptyList(),
private val onItemClick: (MenuItemModel) -> Unit,
private val onItemSelectionChanged: (MenuItemModel) -> Unit,
private val selectedMenuItemsSharedtoCart: SelectedMenuItemsSharedtoCart
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>(){


    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.text_menu_name)
        private val categoryText: TextView = itemView.findViewById(R.id.text_menu_category)
        private val priceText: TextView = itemView.findViewById(R.id.text_menu_price)
        private val imageView: ImageView = itemView.findViewById(R.id.image_menu)
        private val quantityText: TextView = itemView.findViewById(R.id.text_quantity)
        private val checkbox: CheckBox = itemView.findViewById(R.id.checkbox_select)
        private val btnIncrease: Button = itemView.findViewById(R.id.btn_increase)
        private val btnDecrease: Button = itemView.findViewById(R.id.btn_decrease)

        fun bind(item: MenuItemModel) {
            nameText.text = item.name
            categoryText.text = item.category
            priceText.text = "Rs %.2f".format(item.price)
            imageView.setImageBitmap(item.imageBitmap)

            quantityText.text = item.quantity.toString()
            checkbox.isChecked = item.isSelected

            checkbox.setOnCheckedChangeListener { _, isChecked ->
                item.isSelected = isChecked
                onItemSelectionChanged(item)
                if (isChecked) {
                    selectedMenuItemsSharedtoCart.insert(item)
                } else {
                    selectedMenuItemsSharedtoCart.remove(item)
                }
            }

            btnIncrease.setOnClickListener {
                item.quantity++
                quantityText.text = item.quantity.toString()
                if (item.isSelected) onItemSelectionChanged(item)
            }

            btnDecrease.setOnClickListener {
                if (item.quantity > 0) {
                    item.quantity--
                    quantityText.text = item.quantity.toString()
                    if (item.isSelected) onItemSelectionChanged(item)
                }
            }

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }


    fun submitList(newItems: List<MenuItemModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_student_item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size.toInt()
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(items[position])
    }


}