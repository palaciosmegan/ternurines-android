package com.example.testapp.data

import com.example.testapp.data.model.CartItem
import com.example.testapp.data.model.CheckoutDetails
import com.example.testapp.data.model.DELIVERY_DAYS
import com.example.testapp.data.model.Order
import com.example.testapp.data.model.OrderStatus
import com.example.testapp.data.remote.ApiConfig
import com.example.testapp.data.remote.ApiException
import com.example.testapp.data.remote.CartAddRequest
import com.example.testapp.data.remote.CreateOrderRequest
import com.example.testapp.data.remote.CreateOrderResponse
import com.example.testapp.data.remote.TernurinesApi
import com.example.testapp.data.remote.apiCall
import com.example.testapp.data.remote.parseIsoMillis
import com.example.testapp.data.remote.statusFrom
import com.example.testapp.data.remote.toOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

sealed interface OrderResult {
    data class Success(val order: Order) : OrderResult
    data class Error(val message: String) : OrderResult
}

@Singleton
class OrderRepository @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val cartRepository: CartRepository,
    private val authRepository: AuthRepository,
    private val api: TernurinesApi,
    private val config: ApiConfig
) {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private var nextId = 1001

    suspend fun placeOrder(details: CheckoutDetails, items: List<CartItem>): OrderResult =
        if (config.isConfigured) placeRemote(details, items) else OrderResult.Success(placeLocal(details, items))

    /** Trae del servidor los pedidos del usuario con sesión iniciada. */
    suspend fun refreshOrders() {
        if (!config.isConfigured || authRepository.currentUser.value == null) return
        apiCall { api.orders() }
            .onSuccess { list -> _orders.value = list.map { it.toOrder() } }
            .onFailure(::handleSessionError)
    }

    /** Solo para uso futuro del admin / pruebas locales. */
    fun updateStatus(orderId: Int, status: OrderStatus) {
        _orders.update { list -> list.map { if (it.id == orderId) it.copy(status = status) else it } }
    }

    private suspend fun placeRemote(details: CheckoutDetails, items: List<CartItem>): OrderResult =
        apiCall { submit(details, items) }.fold(
            onSuccess = { response ->
                cartRepository.clear()
                catalogRepository.refresh() // el servidor ya descontó el stock
                val order = buildOrder(response, details, items)
                _orders.update { it + order }
                OrderResult.Success(order)
            },
            onFailure = { error ->
                handleSessionError(error)
                OrderResult.Error(
                    if ((error as? ApiException)?.isUnauthorized == true) "Tu sesión venció. Inicia sesión de nuevo."
                    else error.message.orEmpty()
                )
            }
        )

    /** El servidor arma el pedido desde SU carrito: se deja igual al del teléfono y luego se crea el pedido. */
    private suspend fun submit(details: CheckoutDetails, items: List<CartItem>): CreateOrderResponse {
        api.cart().items.forEach { api.removeFromCart(it.product.id) }
        items.forEach { api.addToCart(CartAddRequest(it.product.id, it.quantity)) }
        return api.createOrder(
            CreateOrderRequest(
                phone = details.phone,
                address = details.address,
                district = details.district,
                paymentMethod = details.paymentMethod.name
            )
        )
    }

    private fun buildOrder(response: CreateOrderResponse, details: CheckoutDetails, items: List<CartItem>): Order {
        val now = System.currentTimeMillis()
        return Order(
            id = response.orderId,
            details = details,
            items = items,
            total = items.sumOf { it.subtotal },
            status = statusFrom(response.status),
            placedAt = now,
            estimatedDelivery = parseIsoMillis(response.estimatedDelivery).takeIf { it > 0 }
                ?: (now + DELIVERY_DAYS * DAY_MILLIS)
        )
    }

    private fun placeLocal(details: CheckoutDetails, items: List<CartItem>): Order {
        val now = System.currentTimeMillis()
        val order = Order(
            id = nextId++,
            details = details,
            items = items,
            total = items.sumOf { it.subtotal },
            status = OrderStatus.CONFIRMED,
            placedAt = now,
            estimatedDelivery = now + DELIVERY_DAYS * DAY_MILLIS
        )
        items.forEach { catalogRepository.decreaseStock(it.product.id, it.quantity) }
        cartRepository.clear()
        _orders.update { it + order }
        return order
    }

    private fun handleSessionError(error: Throwable) {
        if ((error as? ApiException)?.isUnauthorized == true) authRepository.expireSession()
    }

    private companion object {
        const val DAY_MILLIS = 24L * 60 * 60 * 1000
    }
}
