package com.example.unieats.backend.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.unieats.backend.dbData.MenuItem
import com.example.unieats.frontend.dashboard.admin.MenuManagement.MenuItemAdmin
import com.example.unieats.frontend.dashboard.student.Menu.MenuItemModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MenuRepository {

    private val database = FirebaseDatabase.getInstance()
    private val menuRef = database.getReference("menu")  // Firebase Realtime Database reference for menu items
    private val storage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference.child("menu_images") // Firebase Storage reference for images

    // LiveData to hold the list of MenuItemModels
    private val _menuItemsLiveData = MutableLiveData<List<MenuItemModel>>()
    val menuItemsLiveData: LiveData<List<MenuItemModel>> get() = _menuItemsLiveData

    // CREATE: Add a new menu item with an image
    // In MenuRepository
    fun addMenuItem(menuItem: MenuItem, imageUri: Uri, callback: (Boolean) -> Unit) {
        val menuItemId = menuRef.push().key

        menuItemId?.let {
            val imageRef = storageRef.child("$menuItemId.jpg")
            imageRef.putFile(imageUri).addOnCompleteListener { uploadTask ->
                if (uploadTask.isSuccessful) {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val updatedMenuItem = menuItem.copy(
                            id = menuItemId.toInt(),
                            imageUrl = uri.toString()
                        )
                        menuRef.child(menuItemId).setValue(updatedMenuItem)
                            .addOnCompleteListener { task ->
                                callback(task.isSuccessful)
                            }
                    }.addOnFailureListener {
                        callback(false)
                    }
                } else {
                    callback(false)
                }
            }
        } ?: callback(false)
    }

    // READ: Fetch the list of all menu items (convert to MenuItemModel with image)
    fun getMenuItems() {
        menuRef.get().addOnSuccessListener { snapshot ->
            val tempList = mutableListOf<MenuItemModel>()
            val children = snapshot.children.toList()
            if (children.isEmpty()) {
                _menuItemsLiveData.postValue(emptyList())
                return@addOnSuccessListener
            }

            var processedCount = 0
            for (child in children) {
                val menuItem = child.getValue(MenuItem::class.java)
                menuItem?.let {
                    MenuItemModel.fromMenuItem(it) { model ->
                        tempList.add(model)
                        processedCount++
                        if (processedCount == children.size) {
                            _menuItemsLiveData.postValue(tempList)
                        }
                    }
                } ?: run {
                    processedCount++
                    if (processedCount == children.size) {
                        _menuItemsLiveData.postValue(tempList)
                    }
                }
            }
        }.addOnFailureListener {
            _menuItemsLiveData.postValue(emptyList())
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
