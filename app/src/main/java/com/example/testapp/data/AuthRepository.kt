package com.example.testapp.data

import com.example.testapp.data.model.User
import com.example.testapp.data.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor() {

    private data class Account(val user: User, val password: String)

    private val accounts = mutableListOf(
        Account(User("Administrador", "admin@ternurines.pe", UserRole.ADMIN), "admin123"),
        Account(User("Cliente Demo", "cliente@ternurines.pe", UserRole.CUSTOMER), "cliente123")
    )

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun login(email: String, password: String): Boolean {
        val account = findAccount(email)?.takeIf { it.password == password } ?: return false
        _currentUser.value = account.user
        return true
    }

    fun register(name: String, email: String, password: String): Boolean {
        if (findAccount(email) != null) return false
        val user = User(name.trim(), email.trim(), UserRole.CUSTOMER)
        accounts.add(Account(user, password))
        return true
    }

    fun logout() {
        _currentUser.value = null
    }

    private fun findAccount(email: String): Account? =
        accounts.find { it.user.email.equals(email.trim(), ignoreCase = true) }
}
