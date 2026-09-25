package com.example.testapp.navigation

object Routes {
    const val HOME = "home"
    const val CART = "cart"
    const val FAVORITES = "favorites"
    const val PROFILE = "profile"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val CHECKOUT = "checkout"
    const val INVENTORY = "admin/inventory"

    const val PRODUCT_ID = "productId"
    const val PRODUCT_DETAIL = "product/{$PRODUCT_ID}"

    fun productDetail(id: String) = "product/$id"

    const val ORDER_ID = "orderId"
    const val ORDER_CONFIRMED = "order-confirmed/{$ORDER_ID}"

    fun orderConfirmed(id: Int) = "order-confirmed/$id"
}
