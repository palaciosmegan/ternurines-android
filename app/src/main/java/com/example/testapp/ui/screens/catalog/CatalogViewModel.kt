package com.example.testapp.ui.screens.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.AccessibilityRepository
import com.example.testapp.data.CartRepository
import com.example.testapp.data.CatalogRepository
import com.example.testapp.data.FavoritesRepository
import com.example.testapp.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class CatalogUiState(
    val products: List<Product> = emptyList(),
    val filters: List<String> = emptyList(),
    val selectedFilter: String = CatalogViewModel.ALL,
    val query: String = "",
    val largeView: Boolean = false,
    val favoriteIds: Set<String> = emptySet()
) {
    val hasActiveFilters: Boolean get() = query.isNotBlank() || selectedFilter != CatalogViewModel.ALL
}

@HiltViewModel
class CatalogViewModel @Inject constructor(
    catalogRepository: CatalogRepository,
    private val accessibilityRepository: AccessibilityRepository,
    private val cartRepository: CartRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val selectedFilter = MutableStateFlow(ALL)

    val uiState: StateFlow<CatalogUiState> = combine(
        catalogRepository.products,
        query,
        selectedFilter,
        accessibilityRepository.settings,
        favoritesRepository.favoriteIds
    ) { products, query, filter, settings, favoriteIds ->
        CatalogUiState(
            products = products.filter { it.matchesFilter(filter) && it.matchesQuery(query) },
            filters = listOf(ALL, FEATURED) + products.map { it.category }.distinct(),
            selectedFilter = filter,
            query = query,
            largeView = settings.largeView,
            favoriteIds = favoriteIds
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CatalogUiState())

    fun onQueryChange(value: String) {
        query.value = value
    }

    fun onFilterSelected(filter: String) {
        selectedFilter.value = filter
    }

    fun clearFilters() {
        query.value = ""
        selectedFilter.value = ALL
    }

    fun addToCart(product: Product): String =
        if (cartRepository.add(product)) "${product.name} agregado al carrito"
        else "No hay más stock disponible de ${product.name}."

    fun toggleFavorite(productId: String) = favoritesRepository.toggle(productId)

    fun toggleLargeView() {
        accessibilityRepository.setLargeView(!uiState.value.largeView)
    }

    private fun Product.matchesFilter(filter: String): Boolean = when (filter) {
        ALL -> true
        FEATURED -> featured
        else -> category == filter
    }

    private fun Product.matchesQuery(query: String): Boolean {
        val term = query.trim()
        if (term.isEmpty()) return true
        return listOfNotNull(name, description, category, subcategory)
            .any { it.contains(term, ignoreCase = true) }
    }

    companion object {
        const val ALL = "Todos"
        const val FEATURED = "Destacados"
    }
}
