package com.example.unieats.frontend.dashboard.Student.Menu

import android.graphics.Bitmap
import android.os.Parcelable
import com.example.unieats.backend.dbData.MenuItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuItemModel(
    val id: String,
    val name: String,
    val category: String,
    val price: Double,
    val quantity: Int,
    val image: Bitmap? // Decoded Bitmap for UI use
) : Parcelable {
    companion object {
        fun fromMenuItem(menuItem: MenuItem, callback: (MenuItemModel) -> Unit) {
            MenuItem.decodeBase64ToBitmap(menuItem.imageUrl)?.let { bitmap ->
                callback(
                    MenuItemModel(
                        id = menuItem.id,
                        name = menuItem.name,
                        category = menuItem.category,
                        price = menuItem.price,
                        quantity = menuItem.quantity,
                        image = bitmap
                    )
                )
            } ?: callback(
                MenuItemModel(
                    id = menuItem.id,
                    name = menuItem.name,
                    category = menuItem.category,
                    price = menuItem.price,
                    quantity = menuItem.quantity,
                    image = null
                )
            )
        }
    }
}
