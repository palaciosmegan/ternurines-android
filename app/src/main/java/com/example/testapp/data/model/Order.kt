package com.example.testapp.data.model

enum class PaymentMethod(val label: String) {
    YAPE("Yape"),
    PLIN("Plin"),
    CARD("Tarjeta de crédito / débito");

    val usesQr: Boolean get() = this != CARD
}

enum class OrderStatus(val label: String) {
    CONFIRMED("Confirmado"),
    PREPARING("Preparando"),
    SHIPPED("Enviado"),
    ON_THE_WAY("En camino"),
    DELIVERED("Entregado")
}

const val DELIVERY_DAYS = 3

data class ShippingInfo(
    val phone: String,
    val address: String,
    val district: String
)

data class CheckoutDetails(
    val fullName: String,
    val email: String,
    val phone: String,
    val address: String,
    val district: String,
    val paymentMethod: PaymentMethod
)

data class Order(
    val id: Int,
    val details: CheckoutDetails,
    val items: List<CartItem>,
    val total: Double,
    val status: OrderStatus,
    val placedAt: Long,
    val estimatedDelivery: Long
)
