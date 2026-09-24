package com.example.testapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomTab(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    Home(Routes.HOME, "Inicio", Icons.Outlined.Home, Icons.Filled.Home),
    Cart(Routes.CART, "Carrito", Icons.Outlined.ShoppingCart, Icons.Filled.ShoppingCart),
    Favorites(Routes.FAVORITES, "Mis favs", Icons.Outlined.FavoriteBorder, Icons.Filled.Favorite),
    Profile(Routes.PROFILE, "Perfil", Icons.Outlined.Person, Icons.Filled.Person);

    companion object {
        val routes = entries.map { it.route }
    }
}
