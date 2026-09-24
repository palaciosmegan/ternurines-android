package com.example.testapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.example.testapp.data.AccessibilityRepository
import com.example.testapp.data.AuthRepository
import com.example.testapp.data.model.AccessibilitySettings
import com.example.testapp.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val accessibilityRepository: AccessibilityRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = authRepository.currentUser
    val accessibility: StateFlow<AccessibilitySettings> = accessibilityRepository.settings

    fun logout() = authRepository.logout()

    fun setLargeView(enabled: Boolean) = accessibilityRepository.setLargeView(enabled)

    fun setHighContrast(enabled: Boolean) = accessibilityRepository.setHighContrast(enabled)
}
