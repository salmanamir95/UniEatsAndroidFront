package com.example.unieats.backend.models

import android.graphics.Bitmap

data class MenuItem(
    val id: Int = 0,
    val name: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val imageUrl: String = "" // Store image URL
)
