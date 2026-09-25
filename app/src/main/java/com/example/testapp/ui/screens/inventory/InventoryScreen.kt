package com.example.testapp.ui.screens.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testapp.data.model.Product
import com.example.testapp.ui.components.AppTopBar
import com.example.testapp.ui.components.EmptyState
import com.example.testapp.ui.components.InventoryRow
import com.example.testapp.ui.components.SectionHeader
import com.example.testapp.ui.components.StockAlertBanner
import com.example.testapp.ui.theme.TestAppTheme

@Composable
fun InventoryRoute(
    onBack: () -> Unit,
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    InventoryScreen(
        state = state,
        onStockInputChange = viewModel::onStockInputChange,
        onSave = viewModel::save,
        onBack = onBack
    )
}

@Composable
fun InventoryScreen(
    state: InventoryUiState,
    onStockInputChange: (String, String) -> Unit,
    onSave: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        AppTopBar(title = "Inventario", onBack = onBack)

        if (state.isAdmin) {
            InventoryList(state, onStockInputChange, onSave)
        } else {
            EmptyState(
                icon = Icons.Outlined.Lock,
                title = "Solo los administradores pueden ver el inventario."
            )
        }
    }
}

@Composable
private fun InventoryList(
    state: InventoryUiState,
    onStockInputChange: (String, String) -> Unit,
    onSave: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionHeader(
                title = "Control de stock",
                subtitle = "Actualiza el stock disponible de cada producto."
            )
        }
        item {
            StockAlertBanner(lowStockProducts = state.lowStockProducts)
        }
        items(state.items, key = { it.product.id }) { item ->
            InventoryRow(
                product = item.product,
                stockInput = item.stockInput,
                error = item.error,
                isSaved = item.isSaved,
                onStockInputChange = { onStockInputChange(item.product.id, it) },
                onSave = { onSave(item.product.id) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InventoryScreenPreview() {
    val product = Product(
        id = "cas-002",
        name = "Casa del Campo Techo Rojo",
        description = "",
        price = 499.0,
        imageUrl = "",
        imageAlt = "",
        stock = 2,
        category = "Casitas y Muebles"
    )
    TestAppTheme {
        InventoryScreen(
            state = InventoryUiState(
                items = listOf(InventoryItemState(product, "-3", error = "El stock no puede ser negativo.")),
                lowStockProducts = listOf(product),
                isAdmin = true
            ),
            onStockInputChange = { _, _ -> },
            onSave = {},
            onBack = {}
        )
    }
}
