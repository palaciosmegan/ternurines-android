package com.example.testapp.util

object Validators {

    private val emailRegex = Regex("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")

    fun isValidEmail(email: String): Boolean = emailRegex.matches(email.trim())

    fun isValidPassword(password: String): Boolean = password.length >= 6

    fun hasMinLength(value: String, min: Int): Boolean = value.trim().length >= min
}
