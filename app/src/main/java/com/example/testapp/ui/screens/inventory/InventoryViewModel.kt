package com.example.testapp.ui.screens.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.AuthRepository
import com.example.testapp.data.CatalogRepository
import com.example.testapp.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class InventoryItemState(
    val product: Product,
    val stockInput: String,
    val error: String? = null,
    val isSaved: Boolean = false
)

data class InventoryUiState(
    val items: List<InventoryItemState> = emptyList(),
    val lowStockProducts: List<Product> = emptyList(),
    val isAdmin: Boolean = false
)

@HiltViewModel
class InventoryViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    private val inputs = MutableStateFlow<Map<String, String>>(emptyMap())
    private val errors = MutableStateFlow<Map<String, String>>(emptyMap())
    private val savedIds = MutableStateFlow<Set<String>>(emptySet())

    val uiState: StateFlow<InventoryUiState> = combine(
        catalogRepository.products,
        inputs,
        errors,
        savedIds,
        authRepository.currentUser
    ) { products, inputs, errors, savedIds, user ->
        InventoryUiState(
            items = products.map { product ->
                InventoryItemState(
                    product = product,
                    stockInput = inputs[product.id] ?: product.stock.toString(),
                    error = errors[product.id],
                    isSaved = product.id in savedIds
                )
            },
            lowStockProducts = products.filter { it.needsRestock },
            isAdmin = user?.isAdmin == true
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), InventoryUiState())

    fun onStockInputChange(productId: String, value: String) {
        inputs.update { it + (productId to value) }
        errors.update { it - productId }
        savedIds.update { it - productId }
    }

    fun save(productId: String) {
        val input = inputs.value[productId] ?: return
        val stock = input.trim().toIntOrNull()

        when {
            stock == null -> showError(productId, "Ingresa un número entero.")
            stock < 0 -> showError(productId, "El stock no puede ser negativo.")
            else -> {
                catalogRepository.updateStock(productId, stock)
                inputs.update { it - productId }
                savedIds.update { it + productId }
            }
        }
    }

    private fun showError(productId: String, message: String) {
        errors.update { it + (productId to message) }
    }
}
