package com.example.unieats.frontend.dashboard.student

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.backend.dbData.User

class SharedStudentViewModel: ViewModel() {
    val user = MutableLiveData<User>()
}