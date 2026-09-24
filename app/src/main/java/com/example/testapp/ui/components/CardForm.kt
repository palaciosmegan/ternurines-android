package com.example.testapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

data class CardInput(
    val number: String = "",
    val expiry: String = "",
    val cvv: String = "",
    val numberError: String? = null,
    val expiryError: String? = null,
    val cvvError: String? = null
)

@Composable
fun CardForm(
    card: CardInput,
    onNumberChange: (String) -> Unit,
    onExpiryChange: (String) -> Unit,
    onCvvChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppTextField(
            value = card.number,
            onValueChange = onNumberChange,
            label = "Número de tarjeta",
            placeholder = "1234 5678 9012 3456",
            error = card.numberError,
            keyboardType = KeyboardType.Number
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            AppTextField(
                value = card.expiry,
                onValueChange = onExpiryChange,
                label = "Vencimiento",
                placeholder = "MM/AA",
                error = card.expiryError,
                keyboardType = KeyboardType.Number,
                modifier = Modifier.weight(1f)
            )
            AppTextField(
                value = card.cvv,
                onValueChange = onCvvChange,
                label = "CVV",
                error = card.cvvError,
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.weight(1f)
            )
        }
    }
}
