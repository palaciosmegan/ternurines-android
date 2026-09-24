package com.example.testapp.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.testapp.ui.components.AppButton
import com.example.testapp.ui.components.AppTextField
import com.example.testapp.ui.components.AppTopBar
import com.example.testapp.ui.components.PasswordField
import com.example.testapp.ui.components.SectionHeader
import com.example.testapp.ui.theme.TestAppTheme

@Composable
fun RegisterRoute(
    onBack: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    RegisterScreen(
        state = viewModel.uiState,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onRegister = viewModel::register,
        onLoginClick = onLoginClick,
        onBack = onBack
    )
}

@Composable
fun RegisterScreen(
    state: RegisterUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegister: () -> Unit,
    onLoginClick: () -> Unit,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        AppTopBar(title = "Crear cuenta", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionHeader(
                title = "Crea tu cuenta",
                subtitle = "Regístrate para guardar tu historial de compras y favoritos."
            )

            AppTextField(
                value = state.name,
                onValueChange = onNameChange,
                label = "Nombre completo",
                error = state.nameError
            )

            AppTextField(
                value = state.email,
                onValueChange = onEmailChange,
                label = "Correo electrónico",
                placeholder = "ejemplo@correo.com",
                error = state.emailError,
                keyboardType = KeyboardType.Email
            )

            PasswordField(
                value = state.password,
                onValueChange = onPasswordChange,
                label = "Contraseña",
                error = state.passwordError
            )

            PasswordField(
                value = state.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = "Confirmar contraseña",
                error = state.confirmPasswordError,
                imeAction = ImeAction.Done
            )

            AppButton(
                text = "Crear cuenta",
                onClick = onRegister,
                modifier = Modifier.fillMaxWidth()
            )

            TextButton(
                onClick = onLoginClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }

    if (state.isRegistered) {
        RegisteredDialog(onConfirm = onLoginClick)
    }
}

@Composable
private fun RegisteredDialog(onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onConfirm,
        icon = {
            Icon(
                Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        },
        title = { Text("¡Cuenta creada con éxito!") },
        text = { Text("Ya puedes iniciar sesión con tu correo y contraseña.") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Iniciar sesión") }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    TestAppTheme {
        RegisterScreen(
            state = RegisterUiState(name = "Ana", confirmPasswordError = "Las contraseñas no coinciden."),
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onRegister = {},
            onLoginClick = {},
            onBack = {}
        )
    }
}
