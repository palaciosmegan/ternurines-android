package com.example.testapp.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.CartRepository
import com.example.testapp.data.model.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class CartUiState(
    val items: List<CartItem> = emptyList()
) {
    val total: Double get() = items.sumOf { it.subtotal }
}

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    val uiState: StateFlow<CartUiState> = cartRepository.items
        .map { CartUiState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CartUiState())

    fun updateQuantity(productId: String, quantity: Int) = cartRepository.updateQuantity(productId, quantity)

    fun remove(productId: String) = cartRepository.remove(productId)
}
