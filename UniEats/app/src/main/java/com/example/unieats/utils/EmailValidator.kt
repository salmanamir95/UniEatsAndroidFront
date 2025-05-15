package com.example.unieats.utils

object EmailValidator {
    private const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)\$"
    fun isValid(email: String): Boolean = email.matches(EMAIL_REGEX.toRegex())
}