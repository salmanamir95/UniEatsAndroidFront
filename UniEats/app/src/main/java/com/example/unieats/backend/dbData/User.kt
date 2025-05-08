package com.example.unieats.backend.dbData

import com.example.unieats.frontend.register.Register

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val age: Int = 0,
    val designation: String = ""
) {
    companion object {
        fun fromRegistertoUser(register: Register, id: String): User {
            return User(
                id = id,
                email = register.email,
                name = register.name,
                age = register.age,
                designation = register.designation
            )
        }
    }
}