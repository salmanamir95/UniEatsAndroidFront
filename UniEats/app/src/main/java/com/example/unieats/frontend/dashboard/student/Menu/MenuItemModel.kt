package com.example.unieats.frontend.dashboard.student.Menu

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.unieats.backend.dbData.MenuItem
import java.net.URL
import java.util.concurrent.Executors

class MenuItemModel(
    val id: Int,
    val name: String,
    val category: String,
    val price: Double,
    val imageUrl: String,
    val imageBitmap: Bitmap? = null // Store the image as Bitmap
) {
    companion object {
        // Convert MenuItem to MenuItemModel with image
        fun toMenuItemModel(menuItem: MenuItem, callback: (MenuItemModel) -> Unit) {
            val executor = Executors.newSingleThreadExecutor()
            executor.execute {
                var bitmap: Bitmap? = null
                try {
                    // Fetch the image from the URL
                    val input = URL(menuItem.imageUrl).openStream()
                    bitmap = BitmapFactory.decodeStream(input)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                // Return a MenuItemModel with the image (and without the quantity)
                callback(MenuItemModel(
                    id = menuItem.id,
                    name = menuItem.name,
                    category = menuItem.category,
                    price = menuItem.price,
                    imageUrl = menuItem.imageUrl,
                    imageBitmap = bitmap
                ))
            }
        }
    }
}