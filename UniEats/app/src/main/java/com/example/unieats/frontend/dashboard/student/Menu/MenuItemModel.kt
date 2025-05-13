package com.example.unieats.frontend.dashboard.student.Menu

import android.graphics.Bitmap
import com.example.unieats.backend.dbData.MenuItem

data class MenuItemModel(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val price: Double = 0.0,
    var quantity: Int = 0,
    val imageBitmap: Bitmap?,
    var isSelected: Boolean = false // <- New field
) {
    companion object {
        fun fromMenuItem(item: MenuItem, callback: (MenuItemModel) -> Unit) {
            // Fetch image and quantity
            MenuItem.fetchImageAndQuantity(item) { bitmap, _ ->
                val model = MenuItemModel(
                    id = item.id,
                    name = item.name,
                    category = item.category,
                    price = item.price,
                    quantity = item.quantity, // Quantity should be fetched from the DB
                    imageBitmap = bitmap
                )
                callback(model)
            }
        }
    }
}
