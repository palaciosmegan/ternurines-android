package com.example.testapp.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
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
fun LoginRoute(
    onBack: () -> Unit,
    onLoggedIn: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.uiState

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) onLoggedIn()
    }

    LoginScreen(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLogin = viewModel::login,
        onRegisterClick = onRegisterClick,
        onBack = onBack
    )
}

@Composable
fun LoginScreen(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onRegisterClick: () -> Unit,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        AppTopBar(title = "Iniciar sesión", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionHeader(
                title = "Bienvenido a Ternurines",
                subtitle = "Inicia sesión para ver tu historial de compras y favoritos."
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
                error = state.passwordError,
                imeAction = ImeAction.Done
            )

            if (state.loginError != null) {
                Text(
                    text = state.loginError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
            }

            AppButton(
                text = "Iniciar sesión",
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth()
            )

            TextButton(
                onClick = onRegisterClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    TestAppTheme {
        LoginScreen(
            state = LoginUiState(email = "cliente@ternurines.pe", loginError = "Correo o contraseña incorrectos."),
            onEmailChange = {},
            onPasswordChange = {},
            onLogin = {},
            onRegisterClick = {},
            onBack = {}
        )
    }
}
