package com.example.testapp.data

import com.example.testapp.data.model.CartItem
import com.example.testapp.data.model.CheckoutDetails
import com.example.testapp.data.model.Order
import com.example.testapp.data.model.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val cartRepository: CartRepository
) {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private var nextId = 1001

    fun placeOrder(details: CheckoutDetails, items: List<CartItem>): Order {
        val order = Order(
            id = nextId++,
            details = details,
            items = items,
            total = items.sumOf { it.subtotal },
            status = OrderStatus.PAID
        )
        items.forEach { catalogRepository.decreaseStock(it.product.id, it.quantity) }
        cartRepository.clear()
        _orders.update { it + order }
        return order
    }
}
