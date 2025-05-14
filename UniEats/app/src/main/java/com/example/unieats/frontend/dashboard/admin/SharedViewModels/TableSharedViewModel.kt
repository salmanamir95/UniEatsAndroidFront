package com.example.unieats.frontend.dashboard.admin.SharedViewModels

import androidx.lifecycle.MutableLiveData
import com.example.unieats.backend.repository.TableRepository

class TableSharedViewModel {
    var data = MutableLiveData<TableRepository>()
}