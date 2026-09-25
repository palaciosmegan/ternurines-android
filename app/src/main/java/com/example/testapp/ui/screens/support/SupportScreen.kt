package com.example.testapp.ui.screens.support

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.testapp.ui.components.AppButton
import com.example.testapp.ui.components.AppButtonStyle
import com.example.testapp.ui.components.AppTextField
import com.example.testapp.ui.components.AppTopBar
import com.example.testapp.ui.theme.TestAppTheme

@Composable
fun SupportRoute(
    onBack: () -> Unit,
    viewModel: SupportViewModel = hiltViewModel()
) {
    SupportScreen(
        state = viewModel.uiState,
        onSubjectChange = viewModel::onSubjectChange,
        onMessageChange = viewModel::onMessageChange,
        onSubmit = viewModel::submit,
        onNewRequest = viewModel::startNewRequest,
        onBack = onBack
    )
}

@Composable
fun SupportScreen(
    state: SupportUiState,
    onSubjectChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onNewRequest: () -> Unit,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        AppTopBar(title = "Centro de soporte", onBack = onBack)

        if (state.submittedRequestId != null) {
            ConfirmationContent(
                requestId = state.submittedRequestId,
                onNewRequest = onNewRequest,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "¿Cómo podemos ayudarte?",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .semantics { heading() }
                )
                Text(
                    "Envíanos tus dudas sobre pedidos, envíos o personalización de productos."
                )
                AppTextField(
                    value = state.subject,
                    onValueChange = onSubjectChange,
                    label = "Asunto",
                    placeholder = "Ej. Consulta sobre mi pedido"
                )
                AppTextField(
                    value = state.message,
                    onValueChange = onMessageChange,
                    label = "Mensaje",
                    placeholder = "Cuéntanos qué necesitas",
                    error = state.messageError,
                    singleLine = false,
                    minLines = 5,
                    imeAction = ImeAction.Default
                )
                AppButton(
                    text = "Enviar consulta",
                    onClick = onSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ConfirmationContent(
    requestId: Int,
    onNewRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { liveRegion = LiveRegionMode.Polite },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text("¡Consulta recibida!", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Registramos tu solicitud correctamente. Nuestro equipo revisará tu mensaje y te responderá pronto."
                )
                Text(
                    "Número de solicitud: #$requestId",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
        AppButton(
            text = "Enviar otra consulta",
            onClick = onNewRequest,
            style = AppButtonStyle.Outlined,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SupportScreenPreview() {
    TestAppTheme {
        SupportScreen(
            state = SupportUiState(),
            onSubjectChange = {},
            onMessageChange = {},
            onSubmit = {},
            onNewRequest = {},
            onBack = {}
        )
    }
}
