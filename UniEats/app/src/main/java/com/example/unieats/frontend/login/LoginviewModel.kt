package com.example.unieats.frontend.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unieats.backend.repository.UserRepository
import com.example.unieats.frontend.dashboard.AppUser
import kotlinx.coroutines.launch
import javax.inject.Inject

// LoginViewModel.kt
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun loginUser(email: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                when (val result = userRepository.loginUser(email, password)) {
                    is UserRepository.Result.Success -> {
                        _loginState.postValue(LoginState.Success(AppUser.from(result.data)))
                    }
                    is UserRepository.Result.Error -> {
                        _loginState.postValue(LoginState.Error(result.message))
                    }
                }
            } catch (e: Exception) {
                _loginState.postValue(LoginState.Error("Login failed: ${e.message}"))
            }
        }
    }
}

sealed class LoginState {
    object Loading : LoginState()
    data class Success(val user: AppUser) : LoginState()
    data class Error(val message: String) : LoginState()
}