package com.example.unieats.frontend.dashboard.admin.SharedViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.backend.repository.TableRepository

class TableSharedViewModel : ViewModel() {
    val data = MutableLiveData<TableRepository>()

    init {
        data.value = TableRepository() // ✅ Immediately initialize
    }
}
