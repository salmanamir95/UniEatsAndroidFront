package com.example.unieats.frontend.dashboard.student.SharedViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.frontend.dashboard.student.Menu.MenuItemModel

class SelectedMenuItemsSharedtoCart: ViewModel() {
    val studentSelection = MutableLiveData<List<MenuItemModel>>(emptyList())

    fun insert(item : MenuItemModel) {
        val currentList = studentSelection.value?.toMutableList() ?: mutableListOf()

        // Add the new item to the list
        currentList.add(item)

        // Update the LiveData with the new list
        studentSelection.value = currentList
    }

    fun remove(item: MenuItemModel) {
        // Get the current list of selected items or create a new list if it's empty
        val currentList = studentSelection.value?.toMutableList() ?: mutableListOf()

        // Remove the item from the list
        currentList.remove(item)

        // Update the LiveData with the new list
        studentSelection.value = currentList
    }

    fun clear() {
        // Clears the list and updates the LiveData
        studentSelection.value = emptyList()
    }
}