package com.example.unieats.backend.CloudinaryManager

import android.content.Context
import com.cloudinary.Configuration
import com.cloudinary.android.MediaManager

object CloudinaryManager {
    fun init(context: Context) {
        val config = Configuration.Builder()
            .setCloudName("diukxf1bo")        // Replace with your Cloudinary cloud name
            .setApiKey("686919468355289")              // Replace with your Cloudinary API key
            .setApiSecret("ElLVjc8ssG1o7gDtT-vSBcJJVbs")        // Replace with your Cloudinary API secret
            .build()

        MediaManager.init(context, config)
    }
}
