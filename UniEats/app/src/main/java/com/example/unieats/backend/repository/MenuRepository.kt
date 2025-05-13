package com.example.unieats.backend.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.unieats.backend.dbData.MenuItem
import com.example.unieats.frontend.dashboard.student.Menu.MenuItemModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class MenuRepository(private val context: Context) {
    private val database = FirebaseDatabase.getInstance()
    private val menuRef = database.getReference("menu")
    private val _menuItemsLiveData = MutableLiveData<List<MenuItemModel>>()
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    val menuItemsLiveData: LiveData<List<MenuItemModel>> get() = _menuItemsLiveData

    // CREATE: Add a new menu item with Base64 image
    fun addMenuItem(menuItem: MenuItem, imageUri: Uri, callback: (Boolean) -> Unit) {
        repositoryScope.launch {
            try {
                val menuItemId = menuRef.push().key ?: run {
                    callback(false)
                    return@launch
                }

                val base64Image = convertImageUriToBase64(imageUri)
                if (base64Image == null) {
                    Log.e("MenuRepository", "Image conversion failed")
                    callback(false)
                    return@launch
                }

                val updatedMenuItem = menuItem.copy(
                    id = menuItemId,
                    imageUrl = base64Image
                )

                // Firebase operation should be on main thread
                menuRef.child(menuItemId).setValue(updatedMenuItem)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("MenuRepository", "Item added successfully")
                        } else {
                            Log.e("MenuRepository", "Firebase error: ${task.exception?.message}")
                        }
                        callback(task.isSuccessful)
                    }
            } catch (e: Exception) {
                Log.e("MenuRepository", "Add menu item error: ${e.message}")
                callback(false)
            }
        }
    }

    // Convert URI to Base64 string with proper threading
    private suspend fun convertImageUriToBase64(uri: Uri): String? = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                    ?: throw IOException("Failed to decode bitmap")

                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 600, 600, true)

                val outputStream = ByteArrayOutputStream()
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
            }
        } catch (e: Exception) {
            Log.e("MenuRepository", "Image conversion error: ${e.message}")
            null
        }
    }


    // READ
    // REAL-TIME LISTENER: Live updates without refresh
    fun observeMenuItems() {
        menuRef.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                repositoryScope.launch {
                    val tempList = mutableListOf<MenuItemModel>()
                    val children = snapshot.children.toList()

                    children.forEach { child ->
                        val menuItem = child.getValue(MenuItem::class.java)
                        menuItem?.let {
                            MenuItemModel.fromMenuItem(it) { model ->
                                tempList.add(model)
                                if (tempList.size == children.size) {
                                    _menuItemsLiveData.postValue(tempList)
                                }
                            }
                        }
                    }

                    // Handle empty state
                    if (children.isEmpty()) {
                        _menuItemsLiveData.postValue(emptyList())
                    }
                }
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                Log.e("MenuRepository", "Real-time listener cancelled: ${error.message}")
                _menuItemsLiveData.postValue(emptyList())
            }
        })



    }

    fun getMenuItemById(menuItemId: String, callback: (MenuItemModel?) -> Unit) {
        menuRef.child(menuItemId).get().addOnSuccessListener { dataSnapshot ->
            val menuItem = dataSnapshot.getValue(MenuItem::class.java)
            menuItem?.let {
                MenuItemModel.fromMenuItem(it) { model ->
                    callback(model)
                }
            } ?: callback(null)
        }.addOnFailureListener { exception ->
            Log.e("MenuRepository", "Error fetching item by ID: ${exception.message}")
            callback(null)
        }
    }


    // UPDATE
    fun updateMenuItem(menuItemId: String, updatedMenuItem: MenuItem, callback: (Boolean) -> Unit) {
        menuRef.child(menuItemId).setValue(updatedMenuItem)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    // DELETE
    fun deleteMenuItem(menuItemId: String, callback: (Boolean) -> Unit) {
        menuRef.child(menuItemId).removeValue()
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    fun updateMenuItem(model: MenuItemModel, callback: (Boolean) -> Unit) {
        val menuItemId = model.id

        if (menuItemId.isBlank()) {
            Log.e("MenuRepository", "Update failed: ID is blank")
            callback(false)
            return
        }

        // Convert Bitmap to Base64
        val base64Image = model.imageBitmap?.let { bitmap ->
            try {
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                val byteArray = outputStream.toByteArray()
                Base64.encodeToString(byteArray, Base64.DEFAULT)
            } catch (e: Exception) {
                Log.e("MenuRepository", "Bitmap to Base64 conversion failed: ${e.message}")
                null
            }
        }

        if (base64Image == null) {
            Log.e("MenuRepository", "Update failed: image conversion failed")
            callback(false)
            return
        }

        val updatedItem = MenuItem(
            id = model.id,
            name = model.name,
            price = model.price,
            category = model.category,
            quantity = model.quantity,
            imageUrl = base64Image
        )

        menuRef.child(menuItemId).setValue(updatedItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("MenuRepository", "Item updated successfully")
                } else {
                    Log.e("MenuRepository", "Update failed: ${task.exception?.message}")
                }
                callback(task.isSuccessful)
            }
    }



}