package com.example.unieats.frontend.dashboard.student.Menu

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.example.unieats.backend.dbData.MenuItem

@Parcelize
data class MenuItemModel(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val price: Double = 0.0,
    var quantity: Int = 0,
    val imageBitmap: Bitmap?,
    var isSelected: Boolean = false
) : Parcelable {
    companion object {
        fun fromMenuItem(item: MenuItem, callback: (MenuItemModel) -> Unit) {
            // Fetch image and quantity
            MenuItem.fetchImageAndQuantity(item) { bitmap, _ ->
                val model = MenuItemModel(
                    id = item.id,
                    name = item.name,
                    category = item.category,
                    price = item.price,
                    quantity = item.quantity,
                    imageBitmap = bitmap
                )
                callback(model)
            }
        }
    }
}
