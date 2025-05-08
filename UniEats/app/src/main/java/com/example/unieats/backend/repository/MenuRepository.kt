package com.example.unieats.backend.repository

import android.net.Uri
import com.example.unieats.backend.models.MenuItem
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class MenuRepository {

    private val database = FirebaseDatabase.getInstance()
    private val menuRef = database.getReference("menu")  // Firebase Realtime Database reference for menu items
    private val storage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference.child("menu_images") // Firebase Storage reference for images

    // CREATE: Add a new menu item with an image
    fun addMenuItem(menuItem: MenuItem, imageUri: Uri, callback: (Boolean) -> Unit) {
        val menuItemId = menuRef.push().key // Generate a unique ID for the menu item

        menuItemId?.let {
            // Upload the image to Firebase Storage
            val imageRef = storageRef.child("$menuItemId.jpg")
            imageRef.putFile(imageUri).addOnCompleteListener { uploadTask ->
                if (uploadTask.isSuccessful) {
                    // Get the image URL after the upload is successful
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Update the menu item with the image URL
                        val menuItemWithImage = menuItem.copy(imageUrl = uri.toString())

                        // Save menu item metadata to Firebase Realtime Database
                        menuRef.child(menuItemId).setValue(menuItemWithImage).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                callback(true)  // Successfully added
                            } else {
                                callback(false)  // Failed to add
                            }
                        }
                    }
                } else {
                    callback(false)  // Image upload failed
                }
            }
        } ?: callback(false)  // Failed to generate unique ID
    }

    // READ: Fetch the list of all menu items (including image URL)
    fun getMenuItems(callback: (List<MenuItem>) -> Unit) {
        menuRef.get().addOnSuccessListener { snapshot ->
            val menuItems = mutableListOf<MenuItem>()
            for (child in snapshot.children) {
                val menuItem = child.getValue(MenuItem::class.java)
                menuItem?.let { menuItems.add(it) }
            }
            callback(menuItems)  // Send the fetched items back to the fragment
        }.addOnFailureListener {
            callback(emptyList())  // If failed to fetch data, return an empty list
        }
    }

    // UPDATE: Update an existing menu item (without changing the image)
    fun updateMenuItem(menuItemId: String, updatedMenuItem: MenuItem, callback: (Boolean) -> Unit) {
        menuRef.child(menuItemId).setValue(updatedMenuItem).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true)  // Successfully updated
            } else {
                callback(false)  // Failed to update
            }
        }
    }

    // DELETE: Remove a menu item (also remove the image from Firebase Storage)
    fun deleteMenuItem(menuItemId: String, callback: (Boolean) -> Unit) {
        // First, delete the image from Firebase Storage
        val imageRef = storageRef.child("$menuItemId.jpg")
        imageRef.delete().addOnCompleteListener { imageDeleteTask ->
            if (imageDeleteTask.isSuccessful) {
                // Then, delete the menu item metadata from Firebase Realtime Database
                menuRef.child(menuItemId).removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callback(true)  // Successfully deleted
                    } else {
                        callback(false)  // Failed to delete menu item
                    }
                }
            } else {
                callback(false)  // Failed to delete image
            }
        }
    }
}
