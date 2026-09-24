package com.example.testapp.ui.screens.checkout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.testapp.ui.components.EmptyState
import com.example.testapp.ui.theme.TestAppTheme

@Composable
fun OrderConfirmedScreen(
    orderId: Int,
    onBackToCatalog: () -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        EmptyState(
            icon = Icons.Outlined.CheckCircle,
            title = "¡Compra completada con éxito!",
            message = "Tu pedido #$orderId fue registrado como pagado y está siendo procesado.",
            actionText = "Volver al catálogo",
            onAction = onBackToCatalog
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderConfirmedPreview() {
    TestAppTheme {
        OrderConfirmedScreen(orderId = 1001, onBackToCatalog = {})
    }
}
