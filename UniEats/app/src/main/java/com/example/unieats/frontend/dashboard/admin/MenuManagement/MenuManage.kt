package com.example.unieats.frontend.dashboard.admin.MenuManagement

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unieats.backend.dbData.MenuItem
import com.example.unieats.backend.repository.MenuRepository
import com.example.unieats.frontend.dashboard.student.Menu.MenuItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuManageViewModel : ViewModel() {
     lateinit var repository: MenuRepository

     val _menuItems = MutableLiveData<List<MenuItemModel>>()
    val menuItems: LiveData<List<MenuItemModel>> get() = _menuItems

    fun isInitialized(): Boolean = this::repository.isInitialized

    fun init(repo: MenuRepository) {
        if (!this::repository.isInitialized) {
            repository = repo
            repo.menuItemsLiveData.observeForever {
                _menuItems.postValue(it)
            }
            loadMenuItems()
        }
    }

    fun loadMenuItems() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMenuItems()
        }
    }

    // In MenuManageViewModel
    fun addMenuItem(menuItemAdmin: MenuItemAdmin, imageUri: Uri, callback: (Boolean) -> Unit) {
        if (this::repository.isInitialized) {
            val menuItem = MenuItem(
                name = menuItemAdmin.name,
                category = menuItemAdmin.category,
                price = menuItemAdmin.price,
                quantity = menuItemAdmin.quantity,
                imageUrl = "" // Temporarily empty, set after upload
            )
            repository.addMenuItem(menuItem, imageUri, callback)
        } else {
            callback(false)
        }
    }

    fun updateMenuItem(itemId: String, updatedItem: MenuItem, callback: (Boolean) -> Unit) {
        repository.updateMenuItem(itemId, updatedItem, callback)
    }

    fun deleteMenuItem(itemId: String, callback: (Boolean) -> Unit) {
        repository.deleteMenuItem(itemId, callback)
    }



}
