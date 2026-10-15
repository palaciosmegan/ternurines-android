package com.example.testapp.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.AuthRepository
import com.example.testapp.data.CartRepository
import com.example.testapp.data.CatalogRepository
import com.example.testapp.data.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppRootViewModel @Inject constructor(
    cartRepository: CartRepository,
    favoritesRepository: FavoritesRepository,
    catalogRepository: CatalogRepository,
    authRepository: AuthRepository
) : ViewModel() {

    /** Se emite cuando el servidor rechaza el token y la app cierra la sesión sola. */
    val sessionExpired: SharedFlow<Unit> = authRepository.sessionExpired

    val cartCount: StateFlow<Int> = cartRepository.itemCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    /** Cuántos favoritos tiene el usuario (los mismos que muestra la pestaña "Mis favs"). */
    val favoritesCount: StateFlow<Int> = favoritesRepository.favorites
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    /** Productos con stock bajo; solo cuenta para administradores, el resto ve 0. */
    val lowStockCount: StateFlow<Int> = combine(
        catalogRepository.lowStockProducts,
        authRepository.currentUser
    ) { lowStock, user ->
        if (user?.isAdmin == true) lowStock.size else 0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
}
