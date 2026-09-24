package com.example.testapp.data.model

enum class UserRole { ADMIN, CUSTOMER }

data class User(
    val name: String,
    val email: String,
    val role: UserRole
) {
    val isAdmin: Boolean get() = role == UserRole.ADMIN
}
