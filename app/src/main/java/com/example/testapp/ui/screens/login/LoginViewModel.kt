package com.example.testapp.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.testapp.data.AuthRepository
import com.example.testapp.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val loginError: String? = null,
    val isLoggedIn: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value, emailError = null, loginError = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, passwordError = null, loginError = null)
    }

    fun login() {
        val emailError = if (Validators.isValidEmail(uiState.email)) null else "Debes ingresar un correo válido."
        val passwordError = if (Validators.isValidPassword(uiState.password)) null
        else "La contraseña debe tener al menos 6 caracteres."

        if (emailError != null || passwordError != null) {
            uiState = uiState.copy(emailError = emailError, passwordError = passwordError)
            return
        }

        uiState = if (authRepository.login(uiState.email, uiState.password)) {
            uiState.copy(isLoggedIn = true)
        } else {
            uiState.copy(loginError = "Correo o contraseña incorrectos.")
        }
    }
}
