package com.example.testapp.ui.screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.testapp.data.AuthRepository
import com.example.testapp.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isRegistered: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun onNameChange(value: String) {
        uiState = uiState.copy(name = value, nameError = null)
    }

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value, emailError = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, passwordError = null)
    }

    fun onConfirmPasswordChange(value: String) {
        uiState = uiState.copy(confirmPassword = value, confirmPasswordError = null)
    }

    fun register() {
        val state = uiState
        val withErrors = state.copy(
            nameError = if (Validators.hasMinLength(state.name, 2)) null else "Debes ingresar tu nombre.",
            emailError = if (Validators.isValidEmail(state.email)) null else "Debes ingresar un correo válido.",
            passwordError = if (Validators.isValidPassword(state.password)) null
            else "La contraseña debe tener al menos 6 caracteres.",
            confirmPasswordError = if (state.confirmPassword == state.password) null
            else "Las contraseñas no coinciden."
        )

        val hasErrors = listOf(
            withErrors.nameError,
            withErrors.emailError,
            withErrors.passwordError,
            withErrors.confirmPasswordError
        ).any { it != null }

        uiState = when {
            hasErrors -> withErrors
            !authRepository.register(state.name, state.email, state.password) ->
                withErrors.copy(emailError = "Ya existe una cuenta con este correo.")
            else -> withErrors.copy(isRegistered = true)
        }
    }
}
