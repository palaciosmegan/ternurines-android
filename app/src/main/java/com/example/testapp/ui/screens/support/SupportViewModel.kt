package com.example.testapp.ui.screens.support

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.testapp.data.AuthRepository
import com.example.testapp.data.SupportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class SupportUiState(
    val subject: String = "",
    val message: String = "",
    val messageError: String? = null,
    val submittedRequestId: Int? = null
)

@HiltViewModel
class SupportViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val supportRepository: SupportRepository
) : ViewModel() {

    var uiState by mutableStateOf(SupportUiState())
        private set

    fun onSubjectChange(value: String) {
        uiState = uiState.copy(subject = value)
    }

    fun onMessageChange(value: String) {
        uiState = uiState.copy(message = value, messageError = null)
    }

    fun submit() {
        if (uiState.message.isBlank()) {
            uiState = uiState.copy(messageError = "Escribe tu consulta para poder enviarla.")
            return
        }

        val request = supportRepository.submitRequest(
            email = authRepository.currentUser.value?.email ?: "cliente no registrado",
            subject = uiState.subject.trim().ifBlank { "Consulta general" },
            message = uiState.message.trim()
        )
        uiState = uiState.copy(submittedRequestId = request.id)
    }

    fun startNewRequest() {
        uiState = SupportUiState()
    }
}
