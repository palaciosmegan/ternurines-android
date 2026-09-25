package com.example.testapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testapp.data.model.Product
import com.example.testapp.ui.theme.TestAppTheme

@Composable
fun StockAlertBanner(
    lowStockProducts: List<Product>,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val hasAlerts = lowStockProducts.isNotEmpty()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) { liveRegion = LiveRegionMode.Polite },
        shape = RoundedCornerShape(20.dp),
        color = if (hasAlerts) colors.errorContainer else colors.secondaryContainer,
        contentColor = if (hasAlerts) colors.onErrorContainer else colors.onSecondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = if (hasAlerts) Icons.Outlined.WarningAmber else Icons.Outlined.CheckCircle,
                contentDescription = null
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (hasAlerts) {
                    Text(
                        text = "Alerta de stock: ${lowStockProducts.size} productos con menos de ${Product.LOW_STOCK_THRESHOLD} unidades",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    lowStockProducts.forEach { product ->
                        Text(
                            text = "• ${product.name}: ${stockLabel(product.stock)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    Text(
                        text = "Todo el stock está en buen nivel.",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun stockLabel(stock: Int): String = when (stock) {
    0 -> "agotado"
    1 -> "1 unidad"
    else -> "$stock unidades"
}

@Preview(showBackground = true)
@Composable
private fun StockAlertBannerPreview() {
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
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            StockAlertBanner(lowStockProducts = listOf(product, product.copy(id = "x", stock = 0)))
            StockAlertBanner(lowStockProducts = emptyList())
        }
    }
}
