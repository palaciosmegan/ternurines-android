package com.example.testapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testapp.data.model.Product

@Composable
fun CatalogProductCard(
    product: Product,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier,
    largeView: Boolean = false
) {
    ProductCard(
        product = product,
        onClick = onClick,
        modifier = modifier,
        largeView = largeView,
        imageActions = {
            FavoriteButton(
                isFavorite = isFavorite,
                onToggle = onToggleFavorite,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            )
        },
        priceActions = {
            AddToCartButton(
                productName = product.name,
                onClick = onAddToCart,
                enabled = !product.isOutOfStock
            )
        }
    )
}
