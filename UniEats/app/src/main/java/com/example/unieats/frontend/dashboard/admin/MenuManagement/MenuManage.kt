package com.example.unieats.frontend.dashboard.admin.MenuManagement

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.backend.dbData.MenuItem
import com.example.unieats.backend.repository.MenuRepository
import com.example.unieats.frontend.dashboard.Student.Menu.MenuItemModel

class MenuManageViewModel : ViewModel() {
    private lateinit var repository: MenuRepository

    private val _menuItems = MutableLiveData<List<MenuItemModel>>()
    val menuItems: LiveData<List<MenuItemModel>> get() = _menuItems

    fun isInitialized(): Boolean = this::repository.isInitialized

    fun init(repo: MenuRepository) {
        if (!this::repository.isInitialized) {
            repository = repo

            // Start observing menu items from Firebase
            repository.observeMenuItems()

            // Observe repository's LiveData and post to ViewModel LiveData
            repository.menuItemsLiveData.observeForever {
                _menuItems.postValue(it)
            }
        }
    }

    fun addMenuItem(menuItemAdmin: MenuItemAdmin, imageUri: Uri, callback: (Boolean) -> Unit) {
        if (isInitialized()) {
            val menuItem = MenuItem(
                name = menuItemAdmin.name,
                category = menuItemAdmin.category,
                price = menuItemAdmin.price,
                quantity = menuItemAdmin.quantity,
                imageUrl = "" // Placeholder, gets filled after image processing
            )
            repository.addMenuItem(menuItem, imageUri, callback)
        } else {
            callback(false)
        }
    }

    fun deleteMenuItem(itemId: String, callback: (Boolean) -> Unit) {
        if (isInitialized()) {
            repository.deleteMenuItem(itemId, callback)
        } else {
            callback(false)
        }
    }
}
