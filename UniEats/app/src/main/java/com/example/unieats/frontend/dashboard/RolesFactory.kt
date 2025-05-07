package com.example.unieats.frontend.dashboard

import com.example.unieats.backend.models.User

sealed class AppUser {
    abstract val id: String
    abstract val name: String

    data class Student(
        override val id: String,
        override val name: String,
    ) : AppUser(){

    }

    data class Staff(
        override val id: String,
        override val name: String,
    ) : AppUser()

    data class Admin(
        override val id: String,
        override val name: String
    ) : AppUser()
}

object AppUserFactory {
    fun from(user: User): AppUser = when (user.designation) {
        "Student" -> AppUser.Student(user.id, user.name)
        "Staff"   -> AppUser.Staff(user.id, user.name)
        "Admin"   -> AppUser.Admin(user.id, user.name)
        else -> throw IllegalArgumentException("Unknown designation: ${user.designation}")
    }
}

