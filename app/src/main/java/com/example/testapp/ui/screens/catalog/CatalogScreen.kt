package com.example.testapp.ui.screens.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.ViewAgenda
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testapp.data.model.Product
import com.example.testapp.ui.components.AddToCartButton
import com.example.testapp.ui.components.CategoryChips
import com.example.testapp.ui.components.EmptyState
import com.example.testapp.ui.components.ProductCard
import com.example.testapp.ui.components.SearchBar
import com.example.testapp.ui.components.SectionHeader
import com.example.testapp.ui.components.rememberShowMessage
import com.example.testapp.ui.theme.TestAppTheme

@Composable
fun CatalogRoute(
    onProductClick: (String) -> Unit,
    viewModel: CatalogViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val showMessage = rememberShowMessage()

    CatalogScreen(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onFilterSelected = viewModel::onFilterSelected,
        onClearFilters = viewModel::clearFilters,
        onToggleLargeView = viewModel::toggleLargeView,
        onProductClick = onProductClick,
        onAddToCart = { showMessage(viewModel.addToCart(it)) }
    )
}

@Composable
fun CatalogScreen(
    state: CatalogUiState,
    onQueryChange: (String) -> Unit,
    onFilterSelected: (String) -> Unit,
    onClearFilters: () -> Unit,
    onToggleLargeView: () -> Unit,
    onProductClick: (String) -> Unit,
    onAddToCart: (Product) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(if (state.largeView) 1 else 2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        fullWidth {
            SectionHeader(
                title = "Colección Ternurines",
                subtitle = "Los auténticos Calico Critters en Perú.",
                modifier = Modifier.padding(top = 16.dp)
            ) {
                FilledTonalIconButton(onClick = onToggleLargeView) {
                    Icon(
                        imageVector = if (state.largeView) Icons.Outlined.GridView else Icons.Outlined.ViewAgenda,
                        contentDescription = if (state.largeView) "Ver en cuadrícula" else "Activar vista ampliada"
                    )
                }
            }
        }

        fullWidth {
            SearchBar(
                query = state.query,
                onQueryChange = onQueryChange,
                placeholder = "Buscar por nombre o categoría..."
            )
        }

        fullWidth {
            CategoryChips(
                categories = state.filters,
                selected = state.selectedFilter,
                onSelect = onFilterSelected,
                contentPadding = PaddingValues(0.dp)
            )
        }

        if (state.products.isEmpty()) {
            fullWidth {
                EmptyState(
                    icon = Icons.Outlined.SearchOff,
                    title = "No se encontraron productos que coincidan con tu búsqueda.",
                    actionText = if (state.hasActiveFilters) "Limpiar filtros" else null,
                    onAction = onClearFilters,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
            }
        } else {
            items(state.products, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    onClick = { onProductClick(product.id) },
                    largeView = state.largeView,
                    priceActions = {
                        AddToCartButton(
                            productName = product.name,
                            onClick = { onAddToCart(product) },
                            enabled = !product.isOutOfStock
                        )
                    }
                )
            }
        }
    }
}

private fun LazyGridScope.fullWidth(content: @Composable () -> Unit) {
    item(span = { GridItemSpan(maxLineSpan) }) { content() }
}

@Preview(showBackground = true)
@Composable
private fun CatalogEmptyPreview() {
    TestAppTheme {
        CatalogScreen(
            state = CatalogUiState(
                filters = listOf("Todos", "Destacados", "Familias", "Bebés"),
                query = "dinosaurio"
            ),
            onQueryChange = {},
            onFilterSelected = {},
            onClearFilters = {},
            onToggleLargeView = {},
            onProductClick = {},
            onAddToCart = {}
        )
    }
}
