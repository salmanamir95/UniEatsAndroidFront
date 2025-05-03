package com.example.unieats.backend.models

import com.example.unieats.frontend.register.Register

data class User(
    val id: Int? = null,
    val email: String? = null,
    val password: String? = null,
    val designation: String? = null,
    val age: Int? = null
){
    companion object {
        fun fromRegistertoUser(register: Register, id: Int): User {
            return User(id, register.email, register.pass, register.designation, register.age)
        }
    }
}