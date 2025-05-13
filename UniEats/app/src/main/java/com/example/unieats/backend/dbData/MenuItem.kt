package com.example.unieats.backend.dbData

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

data class MenuItem(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val imageUrl: String = ""  // This will store the Base64 string of the image
) {
    companion object {
        // Function to convert Bitmap to Base64
        fun encodeImageToBase64(bitmap: Bitmap): String {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }

        // Function to decode Base64 to Bitmap
        fun decodeBase64ToBitmap(base64String: String): Bitmap? {
            return try {
                val decodedByte = Base64.decode(base64String, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        // Fetch image and quantity (decodes Base64 and passes to callback)
        fun fetchImageAndQuantity(menuItem: MenuItem, callback: (Bitmap?, Int) -> Unit) {
            try {
                val bitmap = decodeBase64ToBitmap(menuItem.imageUrl)
                callback(bitmap, menuItem.quantity)
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null, menuItem.quantity)  // Return null in case of error
            }
        }
    }
}
