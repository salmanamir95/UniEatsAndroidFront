package com.example.unieats.frontend.dashboard.student.Menu

import android.graphics.Bitmap
import com.example.unieats.backend.dbData.MenuItem

class MenuItemModel(
    val id: Int = 0,
    val name: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val imageBitmap: Bitmap?
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
    }
}
