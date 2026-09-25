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
                onCheckout = { navController.navigate(Routes.CHECKOUT) }
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
                onSupportClick = { navController.navigate(Routes.SUPPORT) }
            )
        }
        composable(Routes.SUPPORT) {
            SupportRoute(onBack = { navController.popBackStack() })
        }
        composable(Routes.INVENTORY) {
            InventoryRoute(onBack = { navController.popBackStack() })
        }
        composable(Routes.LOGIN) {
            LoginRoute(
                onBack = { navController.popBackStack() },
                onLoggedIn = { navController.popBackStack(Routes.LOGIN, inclusive = true) },
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
