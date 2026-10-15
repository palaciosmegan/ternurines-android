package com.example.testapp.data

import com.example.testapp.data.model.ShippingInfo
import com.example.testapp.data.remote.ApiConfig
import com.example.testapp.data.remote.TernurinesApi
import com.example.testapp.data.remote.apiCall
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Guarda una sola dirección de envío por cuenta (clave: correo).
 * Con API, la dirección también vive en el servidor: se guarda al crear un pedido y se recupera con [fetchRemote].
 */
@Singleton
class ShippingRepository @Inject constructor(
    private val api: TernurinesApi,
    private val config: ApiConfig,
    private val authRepository: AuthRepository
) {

    private val saved = mutableMapOf<String, ShippingInfo>()

    fun get(email: String): ShippingInfo? = saved[key(email)]

    fun save(email: String, info: ShippingInfo) {
        saved[key(email)] = info
    }

    /** Dirección guardada en el servidor para el usuario con sesión (null si no hay, o si falla). */
    suspend fun fetchRemote(): ShippingInfo? {
        val user = authRepository.currentUser.value
        if (!config.isConfigured || user == null) return null
        val info = apiCall { api.address() }.getOrNull()
            ?.takeIf { it.address.isNotBlank() }
            ?.let { ShippingInfo(phone = it.phone, address = it.address, district = it.district) }
        if (info != null) save(user.email, info)
        return info
    }

    private fun key(email: String) = email.trim().lowercase()
}
