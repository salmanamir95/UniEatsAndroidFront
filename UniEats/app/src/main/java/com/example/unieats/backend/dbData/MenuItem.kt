package com.example.unieats.backend.dbData

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.URL
import java.util.concurrent.Executors

data class MenuItem(
    val id: Int = 0,
    val name: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val imageUrl: String = ""
) {
    companion object {
        fun fetchImageAndQuantity(menuItem: MenuItem, callback: (Bitmap?, Int) -> Unit) {
            val executor = Executors.newSingleThreadExecutor()
            executor.execute {
                try {
                    val input = URL(menuItem.imageUrl).openStream()
                    val bitmap = BitmapFactory.decodeStream(input)
                    callback(bitmap, menuItem.quantity)
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback(null, menuItem.quantity)
                }
            }
        }
    }
}
