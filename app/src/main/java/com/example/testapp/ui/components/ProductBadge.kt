package com.example.testapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.testapp.data.model.Product

@Composable
fun ProductBadge(product: Product, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val badge = when {
        product.isOutOfStock -> Badge("Agotado", colors.inverseSurface, colors.inverseOnSurface)
        product.isFewLeft -> Badge("Últimos ${product.stock}", colors.tertiaryContainer, colors.onTertiaryContainer)
        product.featured -> Badge("Destacado", colors.primaryContainer, colors.onPrimaryContainer)
        else -> return
    }

    Text(
        text = badge.text,
        style = MaterialTheme.typography.labelMedium,
        color = badge.contentColor,
        modifier = modifier
            .background(badge.containerColor, CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

private data class Badge(val text: String, val containerColor: Color, val contentColor: Color)
