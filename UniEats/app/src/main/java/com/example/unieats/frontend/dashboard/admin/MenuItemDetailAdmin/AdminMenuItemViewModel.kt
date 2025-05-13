package com.example.unieats.frontend.dashboard.admin.MenuItemDetailAdmin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.backend.dbData.MenuItem
import com.example.unieats.backend.repository.MenuRepository
import com.example.unieats.frontend.dashboard.student.Menu.MenuItemModel

class AdminMenuItemViewModel : ViewModel() {
    private lateinit var menuItem: MenuItemModel
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
        }
    }
    fun updateMenuItem(itemId: String, updatedItem: MenuItem, callback: (Boolean) -> Unit) {
        repository.updateMenuItem(itemId, updatedItem, callback)
    }

    fun deleteMenuItem(menuItemId: String, callback: (Boolean) -> Unit) {
        repository.deleteMenuItem(menuItemId) { success ->
            if (success) {
                Log.d("AdminMenuItemViewModel", "Item deleted successfully")
            } else {
                Log.e("AdminMenuItemViewModel", "Failed to delete item")
            }
            callback(success)
        }
    }


}