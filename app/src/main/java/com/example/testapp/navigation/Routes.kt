package com.example.testapp.navigation

object Routes {
    const val HOME = "home"
    const val CART = "cart"
    const val FAVORITES = "favorites"
    const val PROFILE = "profile"
    const val LOGIN = "login"
    const val REGISTER = "register"

    const val PRODUCT_ID = "productId"
    const val PRODUCT_DETAIL = "product/{$PRODUCT_ID}"

    fun productDetail(id: String) = "product/$id"
}
