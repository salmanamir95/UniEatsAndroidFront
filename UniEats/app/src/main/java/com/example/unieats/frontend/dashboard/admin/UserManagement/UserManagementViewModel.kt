package com.example.unieats.frontend.dashboard.admin.UserManagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unieats.backend.dbData.User
import com.example.unieats.backend.repository.UserRepository
import kotlinx.coroutines.launch

class UserManageViewModel : ViewModel() {
    private lateinit var repository: UserRepository

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private val _operationStatus = MutableLiveData<String>()
    val operationStatus: LiveData<String> get() = _operationStatus

    fun isInitialized(): Boolean = this::repository.isInitialized

    fun init(repo: UserRepository) {
        if (!this::repository.isInitialized) {
            repository = repo

            // Launch coroutine to call suspend function observeUsers()
            viewModelScope.launch {
                repository.observeUsers()
            }

            // Observe the LiveData from repository
            repository.usersLiveData.observeForever {
                _users.postValue(it)
            }
        }
    }

    fun registerUser(
        email: String,
        password: String,
        name: String,
        age: Int,
        designation: String,
        callback: (Boolean) -> Unit
    ) {
        if (isInitialized()) {
            viewModelScope.launch {
                val result = repository.registerUser(email, password, name, age, designation)
                if (result is UserRepository.Result.Success) {
                    _operationStatus.postValue("Registration successful")
                    callback(true)
                } else if (result is UserRepository.Result.Error) {
                    _operationStatus.postValue(result.message)
                    callback(false)
                }
            }
        } else {
            callback(false)
        }
    }

    fun deleteUser(userId: String, callback: (Boolean) -> Unit) {
        if (isInitialized()) {
            viewModelScope.launch {
                val result = repository.deleteUser(userId)
                if (result is UserRepository.Result.Success) {
                    _operationStatus.postValue("User deleted")
                    callback(true)
                } else if (result is UserRepository.Result.Error) {
                    _operationStatus.postValue(result.message)
                    callback(false)
                }
            }
        } else {
            callback(false)
        }
    }
}
