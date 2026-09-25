package com.example.testapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.AccessibilityRepository
import com.example.testapp.data.AuthRepository
import com.example.testapp.data.CatalogRepository
import com.example.testapp.data.model.AccessibilitySettings
import com.example.testapp.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val accessibilityRepository: AccessibilityRepository,
    catalogRepository: CatalogRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = authRepository.currentUser
    val accessibility: StateFlow<AccessibilitySettings> = accessibilityRepository.settings

    val lowStockCount: StateFlow<Int> = catalogRepository.lowStockProducts
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    fun logout() = authRepository.logout()

    fun setLargeView(enabled: Boolean) = accessibilityRepository.setLargeView(enabled)

    fun setHighContrast(enabled: Boolean) = accessibilityRepository.setHighContrast(enabled)
}
