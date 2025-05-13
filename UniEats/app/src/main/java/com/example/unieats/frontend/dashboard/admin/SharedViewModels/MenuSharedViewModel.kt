package com.example.unieats.frontend.dashboard.admin.SharedViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.backend.repository.MenuRepository

class MenuSharedViewModel: ViewModel() {
    public var data = MutableLiveData<MenuRepository>()
}