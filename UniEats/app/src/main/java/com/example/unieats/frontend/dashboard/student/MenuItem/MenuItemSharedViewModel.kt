package com.example.unieats.frontend.dashboard.student.MenuItem

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.frontend.dashboard.student.Menu.MenuItemModel

class MenuItemSharedViewModel: ViewModel() {
    val user = MutableLiveData<MenuItemModel>()
}