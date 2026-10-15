package com.example.testapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.testapp.ui.screens.cart.CartRoute
import com.example.testapp.ui.screens.catalog.CatalogRoute
import com.example.testapp.ui.screens.checkout.CheckoutRoute
import com.example.testapp.ui.screens.checkout.OrderConfirmedScreen
import com.example.testapp.ui.screens.detail.ProductDetailRoute
import com.example.testapp.ui.screens.favorites.FavoritesRoute
import com.example.testapp.ui.screens.inventory.InventoryRoute
import com.example.testapp.ui.screens.login.LoginRoute
import com.example.testapp.ui.screens.orders.OrdersRoute
import com.example.testapp.ui.screens.profile.ProfileRoute
import com.example.testapp.ui.screens.register.RegisterRoute
import com.example.testapp.ui.screens.support.SupportRoute

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
            CatalogRoute(
                onProductClick = { id -> navController.navigate(Routes.productDetail(id)) }
            )
        }
        composable(
            route = Routes.PRODUCT_DETAIL,
            arguments = listOf(navArgument(Routes.PRODUCT_ID) { type = NavType.StringType })
        ) {
            ProductDetailRoute(onBack = { navController.popBackStack() })
        }
        composable(Routes.CART) {
            CartRoute(
                onContinueShopping = { navController.navigateToTab(Routes.HOME) },
                onCheckout = { navController.navigate(Routes.CHECKOUT) },
                onLoginRequired = { navController.navigate(Routes.login(redirect = Routes.CHECKOUT)) }
            )
        }
        composable(Routes.CHECKOUT) {
            CheckoutRoute(
                onBack = { navController.popBackStack() },
                onOrderPlaced = { id ->
                    navController.navigate(Routes.orderConfirmed(id)) {
                        popUpTo(Routes.CHECKOUT) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Routes.ORDER_CONFIRMED,
            arguments = listOf(navArgument(Routes.ORDER_ID) { type = NavType.IntType })
        ) { entry ->
            OrderConfirmedScreen(
                orderId = entry.arguments?.getInt(Routes.ORDER_ID) ?: 0,
                onBackToCatalog = { navController.popBackStack(Routes.HOME, inclusive = false) }
            )
        }
        composable(Routes.FAVORITES) {
            FavoritesRoute(
                onProductClick = { id -> navController.navigate(Routes.productDetail(id)) },
                onExploreCatalog = { navController.navigateToTab(Routes.HOME) },
                onLoginClick = { navController.navigate(Routes.LOGIN) }
            )
        }
        composable(Routes.PROFILE) {
            ProfileRoute(
                onLoginClick = { navController.navigate(Routes.LOGIN) },
                onRegisterClick = { navController.navigate(Routes.REGISTER) },
                onInventoryClick = { navController.navigate(Routes.INVENTORY) },
                onSupportClick = { navController.navigate(Routes.SUPPORT) },
                onOrdersClick = { navController.navigate(Routes.ORDERS) }
            )
        }
        composable(Routes.ORDERS) {
            OrdersRoute(
                onBack = { navController.popBackStack() },
                onExploreCatalog = { navController.navigateToTab(Routes.HOME) }
            )
        }
        composable(Routes.SUPPORT) {
            SupportRoute(onBack = { navController.popBackStack() })
        }
        composable(Routes.INVENTORY) {
            InventoryRoute(onBack = { navController.popBackStack() })
        }
        composable(
            route = Routes.LOGIN_ROUTE,
            arguments = listOf(redirectArgument())
        ) { entry ->
            val redirect = entry.arguments?.getString(Routes.REDIRECT)
            LoginRoute(
                forPurchase = redirect == Routes.CHECKOUT,
                onBack = { navController.popBackStack() },
                onLoggedIn = {
                    if (redirect == null) {
                        navController.popBackStack(Routes.LOGIN_ROUTE, inclusive = true)
                    } else {
                        navController.navigate(redirect) {
                            popUpTo(Routes.LOGIN_ROUTE) { inclusive = true }
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.register(redirect)) {
                        popUpTo(Routes.LOGIN_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Routes.REGISTER_ROUTE,
            arguments = listOf(redirectArgument())
        ) { entry ->
            val redirect = entry.arguments?.getString(Routes.REDIRECT)
            RegisterRoute(
                forPurchase = redirect == Routes.CHECKOUT,
                onBack = { navController.popBackStack() },
                onLoginClick = {
                    navController.navigate(Routes.login(redirect)) {
                        popUpTo(Routes.REGISTER_ROUTE) { inclusive = true }
                    }
                }
            )
        }
    }
}

private fun redirectArgument() = navArgument(Routes.REDIRECT) {
    type = NavType.StringType
    nullable = true
    defaultValue = null
}

fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
