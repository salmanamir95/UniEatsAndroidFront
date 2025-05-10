package com.example.unieats.frontend.dashboard.student.SharedViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.backend.dbData.User

class SharedStudentViewModel: ViewModel() {
    val user = MutableLiveData<User>()
}