package com.example.testapp.data.model

enum class PaymentMethod(val label: String) {
    YAPE("Yape"),
    PLIN("Plin"),
    CARD("Tarjeta de crédito / débito");

    val usesQr: Boolean get() = this != CARD
}

enum class OrderStatus { PAID }

data class CheckoutDetails(
    val fullName: String,
    val email: String,
    val phone: String,
    val address: String,
    val paymentMethod: PaymentMethod
)

data class Order(
    val id: Int,
    val details: CheckoutDetails,
    val items: List<CartItem>,
    val total: Double,
    val status: OrderStatus
)
