package com.example.unieats.backend.CloudinaryManager

import android.content.Context
import com.cloudinary.android.MediaManager

object CloudinaryManager {

    fun init(context: Context) {
        val config: HashMap<String, String> = HashMap()
        config["cloud_name"] = "diukxf1bo"
        config["api_key"] = "686919468355289"
        config["api_secret"] = "YOUR_API_SECRET" // Not required on client
        MediaManager.init(context, config)
    }
}