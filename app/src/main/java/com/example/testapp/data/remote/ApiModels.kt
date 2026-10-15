package com.example.testapp.data.remote

import com.example.testapp.data.model.Product
import com.google.gson.annotations.SerializedName

data class RegisterRequest(val name: String, val email: String, val password: String)

data class LoginRequest(val email: String, val password: String)

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val role: String
)

data class AuthResponse(
    val token: String,
    val user: UserDto
)

data class ErrorResponse(val error: String? = null)

// ---- carrito y favoritos ----

data class CartAddRequest(
    @SerializedName("product_id") val productId: String,
    val quantity: Int
)

data class ProductRefRequest(
    @SerializedName("product_id") val productId: String
)

data class CartLineDto(
    val product: Product,
    val quantity: Int
)

data class CartResponse(val items: List<CartLineDto> = emptyList())

data class FavoriteDto(val product: Product)

// ---- dirección, stock y pedidos ----

data class AddressDto(
    val phone: String = "",
    val address: String = "",
    val district: String = ""
)

data class StockRequest(val stock: Int)

data class CreateOrderRequest(
    val phone: String,
    val address: String,
    val district: String,
    val paymentMethod: String
)

data class CreateOrderResponse(
    @SerializedName("order_id") val orderId: Int,
    val status: String,
    val estimatedDelivery: String
)

data class OrderItemDto(
    @SerializedName("product_id") val productId: String,
    val name: String,
    val unitPrice: Double,
    val quantity: Int,
    val imageUrl: String? = null
)

data class ShippingDto(
    val fullName: String,
    val email: String,
    val phone: String,
    val address: String,
    val district: String
)

data class OrderDto(
    @SerializedName("order_id") val orderId: Int,
    val status: String,
    @SerializedName("created_at") val createdAt: String,
    val estimatedDelivery: String,
    val total: Double,
    val paymentMethod: String,
    val shipping: ShippingDto,
    val items: List<OrderItemDto>
)
