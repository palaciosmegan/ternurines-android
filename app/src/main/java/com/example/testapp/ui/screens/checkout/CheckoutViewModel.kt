package com.example.testapp.ui.screens.checkout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.AuthRepository
import com.example.testapp.data.CartRepository
import com.example.testapp.data.OrderRepository
import com.example.testapp.data.model.CartItem
import com.example.testapp.data.model.CheckoutDetails
import com.example.testapp.data.model.PaymentMethod
import com.example.testapp.ui.components.CardInput
import com.example.testapp.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class CheckoutUiState(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val paymentMethod: PaymentMethod = PaymentMethod.YAPE,
    val card: CardInput = CardInput(),
    val fullNameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val addressError: String? = null,
    val paymentError: String? = null,
    val showQr: Boolean = false,
    val placedOrderId: Int? = null
)

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    authRepository: AuthRepository,
    cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    val items: StateFlow<List<CartItem>> = cartRepository.items
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    var uiState by mutableStateOf(
        CheckoutUiState(
            fullName = authRepository.currentUser.value?.name.orEmpty(),
            email = authRepository.currentUser.value?.email.orEmpty()
        )
    )
        private set

    fun onFullNameChange(value: String) {
        uiState = uiState.copy(fullName = value, fullNameError = null)
    }

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value, emailError = null)
    }

    fun onPhoneChange(value: String) {
        uiState = uiState.copy(phone = value.filter { it.isDigit() }.take(9), phoneError = null)
    }

    fun onAddressChange(value: String) {
        uiState = uiState.copy(address = value, addressError = null)
    }

    fun onPaymentMethodChange(method: PaymentMethod) {
        uiState = uiState.copy(paymentMethod = method, paymentError = null)
    }

    fun onCardNumberChange(value: String) {
        updateCard { copy(number = value.filter { it.isDigit() }.take(16), numberError = null) }
    }

    fun onCardExpiryChange(value: String) {
        updateCard { copy(expiry = value.filter { it.isDigit() || it == '/' }.take(5), expiryError = null) }
    }

    fun onCardCvvChange(value: String) {
        updateCard { copy(cvv = value.filter { it.isDigit() }.take(3), cvvError = null) }
    }

    fun pay() {
        if (!validateCustomer()) return

        when {
            uiState.paymentMethod.usesQr -> uiState = uiState.copy(showQr = true)
            validateCard() -> placeOrder()
            else -> uiState = uiState.copy(
                paymentError = "Los datos de la tarjeta son inválidos. Revísalos e intenta de nuevo."
            )
        }
    }

    fun confirmQrPayment() {
        uiState = uiState.copy(showQr = false)
        placeOrder()
    }

    fun dismissQr() {
        uiState = uiState.copy(showQr = false)
    }

    private fun placeOrder() {
        val state = uiState
        val details = CheckoutDetails(
            fullName = state.fullName.trim(),
            email = state.email.trim(),
            phone = state.phone,
            address = state.address.trim(),
            paymentMethod = state.paymentMethod
        )
        val order = orderRepository.placeOrder(details, items.value)
        uiState = uiState.copy(placedOrderId = order.id)
    }

    private fun validateCustomer(): Boolean {
        val state = uiState
        uiState = state.copy(
            fullNameError = if (Validators.hasMinLength(state.fullName, 3)) null
            else "El nombre es requerido y debe tener al menos 3 letras.",
            emailError = if (Validators.isValidEmail(state.email)) null else "Ingresa un correo válido.",
            phoneError = if (Validators.isValidPhone(state.phone)) null else "Ingresa un celular válido de 9 dígitos.",
            addressError = if (state.address.isNotBlank()) null else "Ingresa tu dirección de envío."
        )
        return listOf(uiState.fullNameError, uiState.emailError, uiState.phoneError, uiState.addressError)
            .all { it == null }
    }

    private fun validateCard(): Boolean {
        val card = uiState.card
        val checked = card.copy(
            numberError = if (Validators.isValidCardNumber(card.number)) null else "Debe tener 16 dígitos.",
            expiryError = if (Validators.isValidExpiry(card.expiry)) null else "Fecha inválida o vencida.",
            cvvError = if (Validators.isValidCvv(card.cvv)) null else "Debe tener 3 dígitos."
        )
        uiState = uiState.copy(card = checked)
        return listOf(checked.numberError, checked.expiryError, checked.cvvError).all { it == null }
    }

    private fun updateCard(change: CardInput.() -> CardInput) {
        uiState = uiState.copy(card = uiState.card.change(), paymentError = null)
    }
}
