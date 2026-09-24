package com.example.testapp.util

import java.util.Calendar

object Validators {

    private val emailRegex = Regex("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")

    fun isValidEmail(email: String): Boolean = emailRegex.matches(email.trim())

    fun isValidPassword(password: String): Boolean = password.length >= 6

    fun hasMinLength(value: String, min: Int): Boolean = value.trim().length >= min

    fun isValidPhone(phone: String): Boolean = Regex("^[0-9]{9}$").matches(phone.trim())

    fun isValidCardNumber(number: String): Boolean = Regex("^[0-9]{16}$").matches(number)

    fun isValidCvv(cvv: String): Boolean = Regex("^[0-9]{3}$").matches(cvv)

    fun isValidExpiry(expiry: String, today: Calendar = Calendar.getInstance()): Boolean {
        val match = Regex("^(0[1-9]|1[0-2])/([0-9]{2})$").matchEntire(expiry) ?: return false
        val month = match.groupValues[1].toInt()
        val year = 2000 + match.groupValues[2].toInt()
        val currentYear = today.get(Calendar.YEAR)
        val currentMonth = today.get(Calendar.MONTH) + 1
        return year > currentYear || (year == currentYear && month >= currentMonth)
    }
}
