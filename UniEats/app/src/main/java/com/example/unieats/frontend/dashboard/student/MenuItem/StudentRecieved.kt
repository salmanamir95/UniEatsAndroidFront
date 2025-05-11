package com.example.unieats.frontend.dashboard.student.MenuItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.unieats.frontend.dashboard.AppUser
import com.example.unieats.frontend.dashboard.student.Menu.MenuItemModel

class StudentRecieved {
    private val _recvd = MutableLiveData<AppUser.Student>()
    val recvd: LiveData<AppUser.Student> = _recvd

    fun setStudent(item: AppUser.Student) {
        _recvd.value = item
    }
}