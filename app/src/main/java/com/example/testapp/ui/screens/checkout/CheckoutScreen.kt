package com.example.testapp.ui.screens.checkout

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testapp.data.model.PaymentMethod
import com.example.testapp.ui.components.AppButton
import com.example.testapp.ui.components.AppTextField
import com.example.testapp.ui.components.AppTopBar
import com.example.testapp.ui.components.CardForm
import com.example.testapp.ui.components.PaymentMethodSelector
import com.example.testapp.ui.components.PaymentQrDialog
import com.example.testapp.ui.components.TotalRow
import com.example.testapp.ui.theme.TestAppTheme

@Composable
fun CheckoutRoute(
    onBack: () -> Unit,
    onOrderPlaced: (Int) -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsStateWithLifecycle()
    val state = viewModel.uiState

    LaunchedEffect(state.placedOrderId) {
        state.placedOrderId?.let(onOrderPlaced)
    }

    CheckoutScreen(
        state = state,
        total = items.sumOf { it.subtotal },
        actions = CheckoutActions(
            onFullNameChange = viewModel::onFullNameChange,
            onEmailChange = viewModel::onEmailChange,
            onPhoneChange = viewModel::onPhoneChange,
            onAddressChange = viewModel::onAddressChange,
            onPaymentMethodChange = viewModel::onPaymentMethodChange,
            onCardNumberChange = viewModel::onCardNumberChange,
            onCardExpiryChange = viewModel::onCardExpiryChange,
            onCardCvvChange = viewModel::onCardCvvChange,
            onPay = viewModel::pay,
            onConfirmQr = viewModel::confirmQrPayment,
            onDismissQr = viewModel::dismissQr,
            onBack = onBack
        )
    )
}

data class CheckoutActions(
    val onFullNameChange: (String) -> Unit = {},
    val onEmailChange: (String) -> Unit = {},
    val onPhoneChange: (String) -> Unit = {},
    val onAddressChange: (String) -> Unit = {},
    val onPaymentMethodChange: (PaymentMethod) -> Unit = {},
    val onCardNumberChange: (String) -> Unit = {},
    val onCardExpiryChange: (String) -> Unit = {},
    val onCardCvvChange: (String) -> Unit = {},
    val onPay: () -> Unit = {},
    val onConfirmQr: () -> Unit = {},
    val onDismissQr: () -> Unit = {},
    val onBack: () -> Unit = {}
)

@Composable
fun CheckoutScreen(
    state: CheckoutUiState,
    total: Double,
    actions: CheckoutActions
) {
    Column(Modifier.fillMaxSize()) {
        AppTopBar(title = "Pago", onBack = actions.onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FormTitle("Datos de compra")

            AppTextField(
                value = state.fullName,
                onValueChange = actions.onFullNameChange,
                label = "Nombre completo",
                error = state.fullNameError
            )
            AppTextField(
                value = state.email,
                onValueChange = actions.onEmailChange,
                label = "Correo electrónico",
                error = state.emailError,
                keyboardType = KeyboardType.Email
            )
            AppTextField(
                value = state.phone,
                onValueChange = actions.onPhoneChange,
                label = "Teléfono (celular)",
                error = state.phoneError,
                keyboardType = KeyboardType.Phone
            )
            AppTextField(
                value = state.address,
                onValueChange = actions.onAddressChange,
                label = "Dirección de envío",
                error = state.addressError,
                imeAction = ImeAction.Done
            )

            FormTitle("Método de pago")

            PaymentMethodSelector(
                selected = state.paymentMethod,
                onSelect = actions.onPaymentMethodChange
            )

            if (state.paymentMethod == PaymentMethod.CARD) {
                CardForm(
                    card = state.card,
                    onNumberChange = actions.onCardNumberChange,
                    onExpiryChange = actions.onCardExpiryChange,
                    onCvvChange = actions.onCardCvvChange,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (state.paymentError != null) {
                Text(
                    text = state.paymentError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Assertive }
                )
            }

            TotalRow(amount = total, modifier = Modifier.padding(vertical = 8.dp))

            AppButton(
                text = "Proceder al pago",
                onClick = actions.onPay,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (state.showQr) {
        PaymentQrDialog(
            method = state.paymentMethod,
            amount = total,
            onConfirm = actions.onConfirmQr,
            onDismiss = actions.onDismissQr
        )
    }
}

@Composable
private fun FormTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(top = 8.dp)
            .semantics { heading() }
    )
}

@Preview(showBackground = true, heightDp = 1100)
@Composable
private fun CheckoutScreenPreview() {
    TestAppTheme {
        CheckoutScreen(
            state = CheckoutUiState(
                paymentMethod = PaymentMethod.CARD,
                paymentError = "Los datos de la tarjeta son inválidos. Revísalos e intenta de nuevo."
            ),
            total = 238.0,
            actions = CheckoutActions()
        )
    }
}
