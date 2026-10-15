package com.example.testapp.ui.screens.checkout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.AuthRepository
import com.example.testapp.data.CartRepository
import com.example.testapp.data.OrderRepository
import com.example.testapp.data.OrderResult
import com.example.testapp.data.ShippingRepository
import com.example.testapp.data.model.CartItem
import com.example.testapp.data.model.CheckoutDetails
import com.example.testapp.data.model.PaymentMethod
import com.example.testapp.data.model.ShippingInfo
import com.example.testapp.ui.components.CardInput
import com.example.testapp.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CheckoutUiState(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val district: String = "",
    val paymentMethod: PaymentMethod = PaymentMethod.YAPE,
    val card: CardInput = CardInput(),
    val phoneError: String? = null,
    val addressError: String? = null,
    val districtError: String? = null,
    val paymentError: String? = null,
    val showQr: Boolean = false,
    val isPlacing: Boolean = false,
    val placedOrderId: Int? = null
)

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    authRepository: AuthRepository,
    cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val shippingRepository: ShippingRepository
) : ViewModel() {

    val items: StateFlow<List<CartItem>> = cartRepository.items
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val user = authRepository.currentUser.value

    var uiState by mutableStateOf(initialState())
        private set

    init {
        // Con API, si no hay dirección guardada en el teléfono, se trae la del servidor.
        viewModelScope.launch {
            val remote = shippingRepository.fetchRemote() ?: return@launch
            val current = uiState
            if (current.phone.isBlank() && current.address.isBlank() && current.district.isBlank()) {
                uiState = current.copy(phone = remote.phone, address = remote.address, district = remote.district)
            }
        }
    }

    private fun initialState(): CheckoutUiState {
        val email = user?.email.orEmpty()
        val saved = shippingRepository.get(email)
        return CheckoutUiState(
            fullName = user?.name.orEmpty(),
            email = email,
            phone = saved?.phone.orEmpty(),
            address = saved?.address.orEmpty(),
            district = saved?.district.orEmpty()
        )
    }

    fun onDistrictChange(value: String) {
        uiState = uiState.copy(district = value, districtError = null)
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
            district = state.district,
            paymentMethod = state.paymentMethod
        )
        if (state.isPlacing) return

        shippingRepository.save(details.email, ShippingInfo(details.phone, details.address, details.district))
        uiState = state.copy(isPlacing = true, paymentError = null)

        viewModelScope.launch {
            uiState = when (val result = orderRepository.placeOrder(details, items.value)) {
                is OrderResult.Success -> uiState.copy(isPlacing = false, placedOrderId = result.order.id)
                is OrderResult.Error -> uiState.copy(isPlacing = false, paymentError = result.message)
            }
        }
    }

    private fun validateCustomer(): Boolean {
        val state = uiState
        uiState = state.copy(
            phoneError = if (Validators.isValidPhone(state.phone)) null else "Ingresa un celular válido de 9 dígitos.",
            addressError = if (state.address.isNotBlank()) null else "Ingresa tu dirección de envío.",
            districtError = if (state.district.isNotBlank()) null else "Selecciona tu distrito."
        )
        return listOf(uiState.phoneError, uiState.addressError, uiState.districtError).all { it == null }
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
