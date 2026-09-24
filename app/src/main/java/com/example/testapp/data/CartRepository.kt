package com.example.testapp.data

import com.example.testapp.data.model.CartItem
import com.example.testapp.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val catalogRepository: CatalogRepository
) {

    private val quantities = MutableStateFlow<Map<String, Int>>(emptyMap())

    val items: Flow<List<CartItem>> = combine(catalogRepository.products, quantities) { products, quantities ->
        products.mapNotNull { product ->
            quantities[product.id]?.let { CartItem(product, it) }
        }
    }

    val itemCount: Flow<Int> = quantities.map { it.values.sum() }

    fun add(product: Product, quantity: Int = 1): Boolean {
        val current = quantities.value[product.id] ?: 0
        val updated = (current + quantity).coerceAtMost(product.stock)
        if (updated <= current) return false
        quantities.update { it + (product.id to updated) }
        return true
    }

    fun updateQuantity(productId: String, quantity: Int) {
        val stock = catalogRepository.products.value.find { it.id == productId }?.stock ?: return
        if (stock <= 0) return remove(productId)
        quantities.update { it + (productId to quantity.coerceIn(1, stock)) }
    }

    fun remove(productId: String) {
        quantities.update { it - productId }
    }

    fun clear() {
        quantities.value = emptyMap()
    }
}
