package com.example.testapp.ui.screens.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.AuthRepository
import com.example.testapp.data.OrderRepository
import com.example.testapp.data.model.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    val orders: StateFlow<List<Order>> = combine(
        orderRepository.orders,
        authRepository.currentUser
    ) { orders, user ->
        orders
            .filter { user != null && it.details.email.equals(user.email, ignoreCase = true) }
            .sortedByDescending { it.placedAt }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        refresh()
    }

    /** Con API, trae los pedidos del servidor (en modo local no hace nada). */
    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            orderRepository.refreshOrders()
            _isLoading.value = false
        }
    }
}
