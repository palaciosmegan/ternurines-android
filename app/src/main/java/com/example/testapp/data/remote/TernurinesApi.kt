package com.example.testapp.data.remote

import com.example.testapp.data.model.Product
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/** Endpoints del API Gateway (ver docs/aws/DISENO.md). El token se agrega solo en el interceptor. */
interface TernurinesApi {

    // ---- auth ----
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse

    // ---- productos ----
    @GET("products")
    suspend fun products(): List<Product>

    @PUT("products/{id}/stock")
    suspend fun updateStock(@Path("id") productId: String, @Body body: StockRequest)

    // ---- carrito ----
    @GET("cart")
    suspend fun cart(): CartResponse

    @POST("cart")
    suspend fun addToCart(@Body body: CartAddRequest)

    @DELETE("cart/{productId}")
    suspend fun removeFromCart(@Path("productId") productId: String)

    // ---- favoritos ----
    @GET("favorites")
    suspend fun favorites(): List<FavoriteDto>

    @POST("favorites")
    suspend fun addFavorite(@Body body: ProductRefRequest)

    @DELETE("favorites/{productId}")
    suspend fun removeFavorite(@Path("productId") productId: String)

    // ---- dirección ----
    @GET("me/address")
    suspend fun address(): AddressDto

    // ---- pedidos ----
    @POST("orders")
    suspend fun createOrder(@Body body: CreateOrderRequest): CreateOrderResponse

    @GET("orders")
    suspend fun orders(): List<OrderDto>
}
