package com.example.testapp.data

import com.example.testapp.data.model.AccessibilitySettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessibilityRepository @Inject constructor() {

    private val _settings = MutableStateFlow(AccessibilitySettings())
    val settings: StateFlow<AccessibilitySettings> = _settings.asStateFlow()

    fun setLargeView(enabled: Boolean) = _settings.update { it.copy(largeView = enabled) }

    fun setHighContrast(enabled: Boolean) = _settings.update { it.copy(highContrast = enabled) }
}
