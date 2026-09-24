package com.example.testapp.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.CatalogRepository
import com.example.testapp.data.model.Product
import com.example.testapp.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    catalogRepository: CatalogRepository
) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle[Routes.PRODUCT_ID])

    val product: StateFlow<Product?> = catalogRepository.product(productId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
