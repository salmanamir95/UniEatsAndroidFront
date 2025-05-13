package com.example.unieats.frontend.dashboard.student.Menu

import android.graphics.Bitmap
import com.example.unieats.backend.dbData.MenuItem
import com.example.unieats.backend.dbData.OrderItem

data class MenuItemModel(
    val id: Int = 0,
    val name: String = "",
    val category: String = "",
    val price: Double = 0.0,
    var quantity: Int = 0,
    val imageBitmap: Bitmap?,
    var isSelected: Boolean = false // <- New field
) {
    companion object {
        fun fromMenuItem(item: MenuItem, callback: (MenuItemModel) -> Unit) {
            MenuItem.fetchImageAndQuantity(item) { bitmap, _ ->
                val model = MenuItemModel(
                    id = item.id,
                    name = item.name,
                    category = item.category,
                    price = item.price,
                    quantity = 0, // Always zero on frontend
                    imageBitmap = bitmap
                )
                callback(model)
            }
        }
        fun toOrderItem(menuItemModel: MenuItemModel): OrderItem {
            return OrderItem(
                itemId = menuItemModel.id.toString(),
                name = menuItemModel.name,
                quantity = menuItemModel.quantity,
                price = menuItemModel.price,
                totalPrice = menuItemModel.price * menuItemModel.quantity
            )
        }

    }
}
