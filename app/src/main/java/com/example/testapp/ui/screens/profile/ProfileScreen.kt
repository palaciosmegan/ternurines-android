package com.example.testapp.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testapp.data.model.User
import com.example.testapp.data.model.UserRole
import com.example.testapp.ui.components.AppButton
import com.example.testapp.ui.components.AppButtonStyle
import com.example.testapp.ui.components.SectionHeader
import com.example.testapp.ui.theme.TestAppTheme

@Composable
fun ProfileRoute(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()

    ProfileScreen(
        user = user,
        onLoginClick = onLoginClick,
        onRegisterClick = onRegisterClick,
        onLogout = viewModel::logout
    )
}

@Composable
fun ProfileScreen(
    user: User?,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (user == null) {
            GuestSection(onLoginClick, onRegisterClick)
        } else {
            UserSection(user, onLogout)
        }
    }
}

@Composable
private fun GuestSection(onLoginClick: () -> Unit, onRegisterClick: () -> Unit) {
    SectionHeader(
        title = "Mi perfil",
        subtitle = "Inicia sesión para ver tu historial de compras y favoritos."
    )
    AppButton(
        text = "Iniciar sesión",
        onClick = onLoginClick,
        modifier = Modifier.fillMaxWidth()
    )
    AppButton(
        text = "Crear cuenta",
        onClick = onRegisterClick,
        style = AppButtonStyle.Outlined,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun UserSection(user: User, onLogout: () -> Unit) {
    SectionHeader(title = "Hola, ${user.name}")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(user.email, style = MaterialTheme.typography.bodyLarge)
            if (user.isAdmin) {
                SuggestionChip(onClick = {}, label = { Text("Admin") })
            }
        }
    }

    AppButton(
        text = "Cerrar sesión",
        onClick = onLogout,
        style = AppButtonStyle.Inverted,
        icon = Icons.AutoMirrored.Outlined.Logout,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfileGuestPreview() {
    TestAppTheme {
        ProfileScreen(user = null, onLoginClick = {}, onRegisterClick = {}, onLogout = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileUserPreview() {
    TestAppTheme {
        ProfileScreen(
            user = User("Administrador", "admin@ternurines.pe", UserRole.ADMIN),
            onLoginClick = {},
            onRegisterClick = {},
            onLogout = {}
        )
    }
}
