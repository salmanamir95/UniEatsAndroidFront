package com.example.unieats.frontend.dashboard.admin.MenuManagement

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unieats.backend.dbData.MenuItem
import com.example.unieats.backend.repository.MenuRepository
import com.example.unieats.frontend.dashboard.student.Menu.MenuItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuManageViewModel() : ViewModel() {
    private lateinit var repository: MenuRepository
    lateinit var menuItems: LiveData<List<MenuItemModel>>
        private set

    fun init(repo: MenuRepository){
        if (!::repository.isInitialized) {
            repository = repo
            menuItems = repo.menuItemsLiveData
        }
    }
    // Fetch menu items from repository
    fun loadMenuItems() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMenuItems()
        }
    }

    // Add menu item
    fun addMenuItem(menuItem: MenuItem, imageUri: Uri, callback: (Boolean) -> Unit) {
        repository.addMenuItem(menuItem, imageUri, callback)
    }

    // Update menu item (metadata only)
    fun updateMenuItem(itemId: String, updatedItem: MenuItem, callback: (Boolean) -> Unit) {
        repository.updateMenuItem(itemId, updatedItem, callback)
    }

    // Delete menu item
    fun deleteMenuItem(itemId: String, callback: (Boolean) -> Unit) {
        repository.deleteMenuItem(itemId, callback)
    }
}
