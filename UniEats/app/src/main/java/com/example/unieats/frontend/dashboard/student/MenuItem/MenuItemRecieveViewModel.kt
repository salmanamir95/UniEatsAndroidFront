package com.example.unieats.frontend.dashboard.student.MenuItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.frontend.dashboard.student.Menu.MenuItemModel

class MenuItemRecieveViewModel: ViewModel() {
    private val _recvd = MutableLiveData<MenuItemModel>()
    val recvd: LiveData<MenuItemModel> = _recvd

    fun setMenuItem(item: MenuItemModel) {
        _recvd.value = item
    }
}