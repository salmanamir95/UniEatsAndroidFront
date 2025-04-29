package com.example.unieats.Login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val userRepository = LoginRepository()

    // LiveData to hold user data
    //val userData: LiveData<User> = userRepository.getUserData()

    // Method to simulate a login process (could call an API, etc.)
    fun login(username: String, password: String) {
        // Business logic for login (validation, API call, etc.)
        // Update LiveData or state
    }
}