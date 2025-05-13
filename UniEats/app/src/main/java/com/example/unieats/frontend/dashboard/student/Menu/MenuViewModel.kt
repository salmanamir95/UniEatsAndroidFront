package com.example.unieats.frontend.dashboard.student.Menu

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unieats.backend.repository.MenuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuViewModel(
    private val repository: MenuRepository
) : ViewModel() {

    // Expose LiveData from repository directly (optional abstraction layer)
    val menuItems: LiveData<List<MenuItemModel>> = repository.menuItemsLiveData
    val selectedItems = MutableLiveData<List<MenuItemModel>>(emptyList())

    fun fetchMenuItems() {
        // Launch fetch from Firebase (already posts to LiveData internally)
        viewModelScope.launch(Dispatchers.IO) {
            repository.observeMenuItems()
        }
    }
    fun updateSelection(item: MenuItemModel) {
        val current = selectedItems.value?.toMutableList() ?: mutableListOf()
        current.removeAll { it.id == item.id }
        if (item.isSelected && item.quantity > 0) {
            current.add(item)
        }
        selectedItems.value = current
    }


}
