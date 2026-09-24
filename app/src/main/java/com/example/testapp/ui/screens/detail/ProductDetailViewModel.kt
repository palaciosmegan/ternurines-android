package com.example.testapp.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.CartRepository
import com.example.testapp.data.CatalogRepository
import com.example.testapp.data.FavoritesRepository
import com.example.testapp.data.model.Product
import com.example.testapp.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    catalogRepository: CatalogRepository,
    private val cartRepository: CartRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle[Routes.PRODUCT_ID])

    val product: StateFlow<Product?> = catalogRepository.product(productId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val isFavorite: StateFlow<Boolean> = favoritesRepository.favoriteIds
        .map { productId in it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    var quantity by mutableIntStateOf(1)
        private set

    fun onQuantityChange(value: Int) {
        val stock = product.value?.stock ?: return
        quantity = value.coerceIn(1, stock.coerceAtLeast(1))
    }

    fun toggleFavorite() = favoritesRepository.toggle(productId)

    fun addToCart(): String {
        val product = product.value ?: return ""
        val added = cartRepository.add(product, quantity)
        quantity = 1
        return if (added) "Agregado al carrito" else "Ya tienes en el carrito todo el stock disponible."
    }
}
