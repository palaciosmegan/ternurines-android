package com.example.testapp.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testapp.data.model.Product
import com.example.testapp.ui.components.CatalogProductCard
import com.example.testapp.ui.components.EmptyState
import com.example.testapp.ui.components.SectionHeader
import com.example.testapp.ui.components.rememberShowMessage
import com.example.testapp.ui.theme.TestAppTheme

@Composable
fun FavoritesRoute(
    onProductClick: (String) -> Unit,
    onExploreCatalog: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val showMessage = rememberShowMessage()

    FavoritesScreen(
        state = state,
        onProductClick = onProductClick,
        onToggleFavorite = viewModel::toggleFavorite,
        onAddToCart = { showMessage(viewModel.addToCart(it)) },
        onExploreCatalog = onExploreCatalog,
        onLoginClick = onLoginClick
    )
}

@Composable
fun FavoritesScreen(
    state: FavoritesUiState,
    onProductClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onAddToCart: (Product) -> Unit,
    onExploreCatalog: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        SectionHeader(
            title = "Mis favoritos",
            subtitle = "Los Ternurines que has marcado con corazón.",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )

        if (!state.isLoggedIn) {
            GuestNotice(onLoginClick)
        }

        if (state.products.isEmpty()) {
            EmptyState(
                icon = Icons.Outlined.FavoriteBorder,
                title = "Todavía no tienes productos favoritos.",
                actionText = "Explorar catálogo",
                onAction = onExploreCatalog
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.products, key = { it.id }) { product ->
                    CatalogProductCard(
                        product = product,
                        isFavorite = true,
                        onClick = { onProductClick(product.id) },
                        onToggleFavorite = { onToggleFavorite(product.id) },
                        onAddToCart = { onAddToCart(product) }
                    )
                }
            }
        }
    }
}

@Composable
private fun GuestNotice(onLoginClick: () -> Unit) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Inicia sesión para guardar tus favoritos en tu cuenta.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextButton(onClick = onLoginClick) { Text("Iniciar sesión") }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesEmptyPreview() {
    TestAppTheme {
        FavoritesScreen(
            state = FavoritesUiState(),
            onProductClick = {},
            onToggleFavorite = {},
            onAddToCart = {},
            onExploreCatalog = {},
            onLoginClick = {}
        )
    }
}
