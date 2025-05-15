package com.example.unieats.utils

object PasswordValidator {
    fun isValid(password: String): Boolean = password.length >= 6
}