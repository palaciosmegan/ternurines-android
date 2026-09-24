package com.example.testapp.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppRootViewModel @Inject constructor(
    cartRepository: CartRepository
) : ViewModel() {

    val cartCount: StateFlow<Int> = cartRepository.itemCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
}
