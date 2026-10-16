package com.example.testapp.data.remote

import com.example.testapp.data.model.Product
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TernurinesApi {

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse

    @GET("products")
    suspend fun products(): List<Product>

    @PUT("products/{id}/stock")
    suspend fun updateStock(@Path("id") productId: String, @Body body: StockRequest)

    @GET("cart")
    suspend fun cart(): CartResponse

    @POST("cart")
    suspend fun addToCart(@Body body: CartAddRequest)

    @DELETE("cart/{productId}")
    suspend fun removeFromCart(@Path("productId") productId: String)

    @GET("favorites")
    suspend fun favorites(): List<FavoriteDto>

    @POST("favorites")
    suspend fun addFavorite(@Body body: ProductRefRequest)

    @DELETE("favorites/{productId}")
    suspend fun removeFavorite(@Path("productId") productId: String)

    @GET("me/address")
    suspend fun address(): AddressDto

    @POST("orders")
    suspend fun createOrder(@Body body: CreateOrderRequest): CreateOrderResponse

    @GET("orders")
    suspend fun orders(): List<OrderDto>
}
