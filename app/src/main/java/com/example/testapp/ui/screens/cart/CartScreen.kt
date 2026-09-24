package com.example.testapp.ui.screens.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testapp.data.model.CartItem
import com.example.testapp.data.model.Product
import com.example.testapp.ui.components.AppButton
import com.example.testapp.ui.components.CartItemRow
import com.example.testapp.ui.components.EmptyState
import com.example.testapp.ui.components.SectionHeader
import com.example.testapp.ui.components.TotalRow
import com.example.testapp.ui.theme.TestAppTheme

@Composable
fun CartRoute(
    onContinueShopping: () -> Unit,
    onCheckout: () -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CartScreen(
        state = state,
        onQuantityChange = viewModel::updateQuantity,
        onRemove = viewModel::remove,
        onContinueShopping = onContinueShopping,
        onCheckout = onCheckout
    )
}

@Composable
fun CartScreen(
    state: CartUiState,
    onQuantityChange: (String, Int) -> Unit,
    onRemove: (String) -> Unit,
    onContinueShopping: () -> Unit,
    onCheckout: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        SectionHeader(
            title = "Tu carrito",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        if (state.items.isEmpty()) {
            EmptyState(
                icon = Icons.Outlined.ShoppingCart,
                title = "Tu carrito está vacío.",
                actionText = "Seguir comprando",
                onAction = onContinueShopping
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.items, key = { it.product.id }) { item ->
                    CartItemRow(
                        item = item,
                        onQuantityChange = { onQuantityChange(item.product.id, it) },
                        onRemove = { onRemove(item.product.id) }
                    )
                }
            }

            CartSummary(total = state.total, onCheckout = onCheckout)
        }
    }
}

@Composable
private fun CartSummary(total: Double, onCheckout: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TotalRow(amount = total)
            AppButton(
                text = "Ir a pagar",
                onClick = onCheckout,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CartScreenPreview() {
    val product = Product(
        id = "fam-001",
        name = "Familia Osita Galleta",
        description = "",
        price = 119.0,
        imageUrl = "",
        imageAlt = "Familia de ositos",
        stock = 15,
        category = "Familias"
    )
    TestAppTheme {
        CartScreen(
            state = CartUiState(listOf(CartItem(product, 2))),
            onQuantityChange = { _, _ -> },
            onRemove = {},
            onContinueShopping = {},
            onCheckout = {}
        )
    }
}
