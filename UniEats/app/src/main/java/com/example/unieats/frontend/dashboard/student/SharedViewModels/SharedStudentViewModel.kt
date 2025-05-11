package com.example.unieats.frontend.dashboard.student.SharedViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.backend.dbData.User
import com.example.unieats.frontend.dashboard.AppUser

class SharedStudentViewModel: ViewModel() {
    val user = MutableLiveData<AppUser.Student>()

    fun setStudent(student: AppUser.Student) {
        user.value = student
    }
}