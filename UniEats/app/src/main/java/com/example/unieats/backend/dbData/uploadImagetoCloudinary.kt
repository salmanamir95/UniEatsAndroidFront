package com.example.unieats.backend.dbData

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

fun uploadImageToCloudinary(context: Context, imageUri: Uri, onResult: (String?) -> Unit) {
    val requestId = MediaManager.get().upload(imageUri)
        .option("resource_type", "image")
        .callback(object : UploadCallback {
            override fun onStart(requestId: String?) {}

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

            override fun onSuccess(requestId: String?, resultData: Map<*, *>) {
                val imageUrl = resultData["secure_url"] as? String
                onResult(imageUrl) // Success - return Cloudinary URL
            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                onResult(null) // Failure
            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
        })
        .dispatch()
}
