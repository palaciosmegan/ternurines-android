package com.example.testapp.data.remote

import com.example.testapp.data.model.CartItem
import com.example.testapp.data.model.CheckoutDetails
import com.example.testapp.data.model.Order
import com.example.testapp.data.model.OrderStatus
import com.example.testapp.data.model.PaymentMethod
import com.example.testapp.data.model.Product
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/** Convierte una fecha ISO como "2026-10-15T14:03:00Z" a milisegundos (0 si no se puede leer). */
fun parseIsoMillis(value: String): Long = try {
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        .apply { timeZone = TimeZone.getTimeZone("UTC") }
        .parse(value)?.time ?: 0L
} catch (e: java.text.ParseException) {
    0L
}

fun OrderDto.toOrder(): Order = Order(
    id = orderId,
    details = CheckoutDetails(
        fullName = shipping.fullName,
        email = shipping.email,
        phone = shipping.phone,
        address = shipping.address,
        district = shipping.district,
        paymentMethod = paymentMethodFrom(paymentMethod)
    ),
    items = items.map { item ->
        CartItem(
            product = Product(
                id = item.productId,
                name = item.name,
                description = "",
                price = item.unitPrice,
                imageUrl = item.imageUrl.orEmpty(),
                imageAlt = item.name,
                stock = 0,
                category = ""
            ),
            quantity = item.quantity
        )
    },
    total = total,
    status = statusFrom(status),
    placedAt = parseIsoMillis(createdAt),
    estimatedDelivery = parseIsoMillis(estimatedDelivery)
)

fun statusFrom(value: String): OrderStatus =
    OrderStatus.entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: OrderStatus.CONFIRMED

private fun paymentMethodFrom(value: String): PaymentMethod =
    PaymentMethod.entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: PaymentMethod.YAPE
