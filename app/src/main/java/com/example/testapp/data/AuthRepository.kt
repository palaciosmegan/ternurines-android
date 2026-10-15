package com.example.testapp.data

import com.example.testapp.data.model.User
import com.example.testapp.data.model.UserRole
import com.example.testapp.data.remote.ApiConfig
import com.example.testapp.data.remote.ApiException
import com.example.testapp.data.remote.LoginRequest
import com.example.testapp.data.remote.RegisterRequest
import com.example.testapp.data.remote.SessionStore
import com.example.testapp.data.remote.TernurinesApi
import com.example.testapp.data.remote.apiCall
import com.example.testapp.data.remote.roleFrom
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed interface AuthResult {
    data object Success : AuthResult
    data class Error(val message: String, val emailTaken: Boolean = false) : AuthResult
}

/**
 * Con `api.baseUrl` configurada habla con el API de AWS y guarda la sesión en el teléfono.
 * Sin URL, funciona en modo local con cuentas en memoria (demo).
 */
@Singleton
class AuthRepository @Inject constructor(
    private val api: TernurinesApi,
    private val session: SessionStore,
    private val config: ApiConfig
) {

    private data class Account(val user: User, val password: String)

    private val localAccounts = mutableListOf(
        Account(User("Administrador", "admin@ternurines.pe", UserRole.ADMIN), "admin123"),
        Account(User("Cliente Demo", "cliente@ternurines.pe", UserRole.CUSTOMER), "cliente123")
    )

    private val _currentUser = MutableStateFlow(if (config.isConfigured) session.loadUser() else null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _sessionExpired = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val sessionExpired: SharedFlow<Unit> = _sessionExpired.asSharedFlow()

    suspend fun login(email: String, password: String): AuthResult =
        if (config.isConfigured) loginRemote(email, password) else loginLocal(email, password)

    suspend fun register(name: String, email: String, password: String): AuthResult =
        if (config.isConfigured) registerRemote(name, email, password) else registerLocal(name, email, password)

    fun logout() {
        session.clear()
        _currentUser.value = null
    }

    /** El servidor rechazó el token (venció o ya no es válido): cierra la sesión y avisa a la pantalla. */
    fun expireSession() {
        if (_currentUser.value == null) return // varias llamadas pueden fallar a la vez: avisar una sola vez
        logout()
        _sessionExpired.tryEmit(Unit)
    }

    private suspend fun loginRemote(email: String, password: String): AuthResult =
        apiCall { api.login(LoginRequest(email.trim(), password)) }.fold(
            onSuccess = { response ->
                session.save(response.token, response.user)
                _currentUser.value = User(
                    name = response.user.name,
                    email = response.user.email,
                    role = roleFrom(response.user.role),
                    id = response.user.id
                )
                AuthResult.Success
            },
            onFailure = { AuthResult.Error(it.message.orEmpty()) }
        )

    private suspend fun registerRemote(name: String, email: String, password: String): AuthResult =
        apiCall { api.register(RegisterRequest(name.trim(), email.trim(), password)) }.fold(
            // Se ignora el token: después de crear la cuenta el usuario inicia sesión (flujo actual de la app).
            onSuccess = { AuthResult.Success },
            onFailure = { AuthResult.Error(it.message.orEmpty(), emailTaken = (it as? ApiException)?.code == 409) }
        )

    private fun loginLocal(email: String, password: String): AuthResult {
        val account = findLocal(email)?.takeIf { it.password == password }
            ?: return AuthResult.Error("Correo o contraseña incorrectos.")
        _currentUser.value = account.user
        return AuthResult.Success
    }

    private fun registerLocal(name: String, email: String, password: String): AuthResult {
        if (findLocal(email) != null) {
            return AuthResult.Error("Ya existe una cuenta con este correo.", emailTaken = true)
        }
        localAccounts.add(Account(User(name.trim(), email.trim(), UserRole.CUSTOMER), password))
        return AuthResult.Success
    }

    private fun findLocal(email: String): Account? =
        localAccounts.find { it.user.email.equals(email.trim(), ignoreCase = true) }
}
