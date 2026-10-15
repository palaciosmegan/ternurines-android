package com.example.testapp.ui.screens.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testapp.data.model.CartItem
import com.example.testapp.data.model.CheckoutDetails
import com.example.testapp.data.model.Order
import com.example.testapp.data.model.OrderStatus
import com.example.testapp.data.model.PaymentMethod
import com.example.testapp.data.model.Product
import com.example.testapp.ui.components.AppTopBar
import com.example.testapp.ui.components.EmptyState
import com.example.testapp.ui.theme.TestAppTheme
import com.example.testapp.util.formatPrice
import com.example.testapp.util.formatShortDate

@Composable
fun OrdersRoute(
    onBack: () -> Unit,
    onExploreCatalog: () -> Unit,
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    OrdersScreen(orders = orders, isLoading = isLoading, onBack = onBack, onExploreCatalog = onExploreCatalog)
}

@Composable
fun OrdersScreen(
    orders: List<Order>,
    onBack: () -> Unit,
    onExploreCatalog: () -> Unit,
    isLoading: Boolean = false
) {
    Column(Modifier.fillMaxSize()) {
        AppTopBar(title = "Mis compras", onBack = onBack)

        if (orders.isEmpty() && isLoading) {
            Text(
                text = "Cargando tus compras…",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(24.dp)
            )
        } else if (orders.isEmpty()) {
            EmptyState(
                icon = Icons.Outlined.ReceiptLong,
                title = "Aún no tienes compras.",
                message = "Cuando compres algo, aquí podrás ver cuándo llega.",
                actionText = "Ver catálogo",
                onAction = onExploreCatalog
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orders, key = { it.id }) { OrderCard(it) }
            }
        }
    }
}

@Composable
private fun OrderCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Pedido #${order.id}", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Comprado el ${formatShortDate(order.placedAt)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                SuggestionChip(onClick = {}, label = { Text(order.status.label) })
            }

            order.items.forEach { item ->
                Text("${item.quantity} × ${item.product.name}", style = MaterialTheme.typography.bodyMedium)
            }

            DeliveryProgress(order)

            Text(
                text = "Enviamos a: ${order.details.address}, ${order.details.district}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Total: ${formatPrice(order.total)}",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
private fun DeliveryProgress(order: Order) {
    val delivered = order.status == OrderStatus.DELIVERED
    val steps = OrderStatus.entries.size

    Text(
        text = if (delivered) "Tu pedido ya llegó." else "Llega el ${formatShortDate(order.estimatedDelivery)}",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.primary
    )
    LinearProgressIndicator(
        progress = { (order.status.ordinal + 1f) / steps },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun OrdersScreenPreview() {
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
    val now = System.currentTimeMillis()
    TestAppTheme {
        OrdersScreen(
            orders = listOf(
                Order(
                    id = 1001,
                    details = CheckoutDetails(
                        "Cliente Demo", "cliente@ternurines.pe", "999888777",
                        "Av. Larco 123", "Miraflores", PaymentMethod.YAPE
                    ),
                    items = listOf(CartItem(product, 2)),
                    total = 238.0,
                    status = OrderStatus.PREPARING,
                    placedAt = now,
                    estimatedDelivery = now + 3L * 24 * 60 * 60 * 1000
                )
            ),
            onBack = {},
            onExploreCatalog = {}
        )
    }
}
