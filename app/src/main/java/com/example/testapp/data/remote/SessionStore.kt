package com.example.testapp.data.remote

import android.content.Context
import androidx.core.content.edit
import com.example.testapp.data.model.User
import com.example.testapp.data.model.UserRole
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Guarda la sesión (token JWT y datos básicos) en el teléfono para no pedir login cada vez. */
@Singleton
class SessionStore @Inject constructor(
    @ApplicationContext context: Context
) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    val token: String? get() = prefs.getString(KEY_TOKEN, null)

    fun save(token: String, user: UserDto) {
        prefs.edit {
            putString(KEY_TOKEN, token)
            putString(KEY_ID, user.id)
            putString(KEY_NAME, user.name)
            putString(KEY_EMAIL, user.email)
            putString(KEY_ROLE, user.role)
        }
    }

    fun loadUser(): User? {
        if (token == null) return null
        val email = prefs.getString(KEY_EMAIL, null) ?: return null
        return User(
            name = prefs.getString(KEY_NAME, "").orEmpty(),
            email = email,
            role = roleFrom(prefs.getString(KEY_ROLE, null)),
            id = prefs.getString(KEY_ID, "").orEmpty()
        )
    }

    fun clear() {
        prefs.edit { clear() }
    }

    private companion object {
        const val PREFS_NAME = "session"
        const val KEY_TOKEN = "token"
        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_EMAIL = "email"
        const val KEY_ROLE = "role"
    }
}

fun roleFrom(value: String?): UserRole =
    if (value.equals("admin", ignoreCase = true)) UserRole.ADMIN else UserRole.CUSTOMER
