package com.example.testapp.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.AuthRepository
import com.example.testapp.data.CartRepository
import com.example.testapp.data.FavoritesRepository
import com.example.testapp.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class FavoritesUiState(
    val products: List<Product> = emptyList(),
    val isLoggedIn: Boolean = false
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val favoritesRepository: FavoritesRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> = combine(
        favoritesRepository.favorites,
        authRepository.currentUser
    ) { products, user ->
        FavoritesUiState(products = products, isLoggedIn = user != null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FavoritesUiState())

    fun toggleFavorite(productId: String) = favoritesRepository.toggle(productId)

    fun addToCart(product: Product): String =
        if (cartRepository.add(product)) "${product.name} agregado al carrito"
        else "No hay más stock disponible de ${product.name}."
}
