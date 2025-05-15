package com.example.unieats.frontend.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unieats.backend.repository.UserRepository
import com.example.unieats.backend.dbData.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val user: User) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> get() = _registerState

    fun registerUser(email: String, password: String, name: String, age: Int, designation: String) {
        _registerState.value = RegisterState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = userRepository.registerUser(email, password, name, age, designation)
            _registerState.value = when(result) {
                is UserRepository.Result.Success -> RegisterState.Success(result.data)
                is UserRepository.Result.Error -> RegisterState.Error(result.message)
            }
        }
    }
}
