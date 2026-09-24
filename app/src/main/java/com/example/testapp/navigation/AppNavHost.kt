package com.example.testapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.testapp.ui.components.EmptyState
import com.example.testapp.ui.screens.login.LoginRoute
import com.example.testapp.ui.screens.profile.ProfileRoute
import com.example.testapp.ui.screens.register.RegisterRoute

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        composable(Routes.HOME) {
            EmptyState(icon = Icons.Outlined.Home, title = "Catálogo")
        }
        composable(Routes.CART) {
            EmptyState(icon = Icons.Outlined.ShoppingCart, title = "Carrito")
        }
        composable(Routes.FAVORITES) {
            EmptyState(icon = Icons.Outlined.FavoriteBorder, title = "Mis favs")
        }
        composable(Routes.PROFILE) {
            ProfileRoute(
                onLoginClick = { navController.navigate(Routes.LOGIN) },
                onRegisterClick = { navController.navigate(Routes.REGISTER) }
            )
        }
        composable(Routes.LOGIN) {
            LoginRoute(
                onBack = { navController.popBackStack() },
                onLoggedIn = { navController.navigateToTab(Routes.HOME) },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.REGISTER) {
            RegisterRoute(
                onBack = { navController.popBackStack() },
                onLoginClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }
    }
}

fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
