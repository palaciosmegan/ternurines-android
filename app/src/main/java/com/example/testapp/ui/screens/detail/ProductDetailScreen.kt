package com.example.testapp.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testapp.data.model.Product
import com.example.testapp.ui.components.AppButton
import com.example.testapp.ui.components.AppTopBar
import com.example.testapp.ui.components.EmptyState
import com.example.testapp.ui.components.ProductBadge
import com.example.testapp.ui.components.ProductImage
import com.example.testapp.ui.components.QuantityStepper
import com.example.testapp.ui.components.rememberShowMessage
import com.example.testapp.ui.theme.TestAppTheme
import com.example.testapp.util.formatPrice

@Composable
fun ProductDetailRoute(
    onBack: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val product by viewModel.product.collectAsStateWithLifecycle()
    val showMessage = rememberShowMessage()

    ProductDetailScreen(
        product = product,
        quantity = viewModel.quantity,
        onQuantityChange = viewModel::onQuantityChange,
        onAddToCart = { showMessage(viewModel.addToCart()) },
        onBack = onBack
    )
}

@Composable
fun ProductDetailScreen(
    product: Product?,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onAddToCart: () -> Unit,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        AppTopBar(title = "Volver al catálogo", onBack = onBack)

        if (product == null) {
            EmptyState(icon = Icons.Outlined.SearchOff, title = "No se encontró este producto.")
        } else {
            ProductDetailContent(product, quantity, onQuantityChange, onAddToCart)
        }
    }
}

@Composable
private fun ProductDetailContent(
    product: Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onAddToCart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box {
            ProductImage(
                url = product.imageUrl,
                contentDescription = product.imageAlt,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(28.dp))
            )
            ProductBadge(
                product = product,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            )
        }

        Text(
            text = listOfNotNull(product.category, product.subcategory).joinToString(" · "),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )

        Text(
            text = product.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.semantics { heading() }
        )

        Text(
            text = formatPrice(product.price),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(product.description, style = MaterialTheme.typography.bodyLarge)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            product.ageMin?.let { InfoChip("+$it años", Icons.Outlined.Cake) }
            product.pieces?.let { InfoChip("$it piezas") }
        }

        Text(
            text = if (product.isOutOfStock) "Este producto está agotado por ahora." else "${product.stock} disponibles",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (!product.isOutOfStock) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                QuantityStepper(
                    quantity = quantity,
                    onQuantityChange = onQuantityChange,
                    max = product.stock
                )
                AppButton(
                    text = "Agregar al carrito",
                    onClick = onAddToCart,
                    icon = Icons.Outlined.ShoppingCart,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun InfoChip(text: String, icon: ImageVector? = null) {
    AssistChip(
        onClick = {},
        label = { Text(text) },
        leadingIcon = icon?.let { { Icon(it, contentDescription = null) } }
    )
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailPreview() {
    TestAppTheme {
        ProductDetailScreen(
            product = Product(
                id = "fam-001",
                name = "Familia Osita Galleta",
                description = "Cuatro ositos café con ropa removible: papá, mamá y dos bebés.",
                price = 119.0,
                imageUrl = "",
                imageAlt = "Familia de ositos",
                stock = 15,
                category = "Familias",
                subcategory = "Osos",
                ageMin = 3,
                pieces = 4,
                featured = true
            ),
            quantity = 1,
            onQuantityChange = {},
            onAddToCart = {},
            onBack = {}
        )
    }
}
