package com.example.unieats.frontend.dashboard.student.SharedViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.frontend.dashboard.student.Menu.MenuItemModel

class MenuItemSharedViewModel: ViewModel() {
    val menuItem = MutableLiveData<MenuItemModel>()
}