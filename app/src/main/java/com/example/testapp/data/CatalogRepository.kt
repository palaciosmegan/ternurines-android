package com.example.testapp.data

import android.content.Context
import com.example.testapp.data.model.Product
import com.example.testapp.data.remote.ApiConfig
import com.example.testapp.data.remote.ApiException
import com.example.testapp.data.remote.StockRequest
import com.example.testapp.data.remote.TernurinesApi
import com.example.testapp.data.remote.apiCall
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: TernurinesApi,
    private val config: ApiConfig
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // Arranca con el catálogo incluido en la app y, si hay API, se reemplaza con el del servidor.
    private val _products = MutableStateFlow(loadProducts())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    init {
        refresh()
    }

    /** Pide el catálogo al API. Si falla o llega vacío, se queda el catálogo local. */
    fun refresh() {
        if (!config.isConfigured) return
        scope.launch {
            apiCall { api.products() }.onSuccess { remote ->
                if (remote.isNotEmpty()) _products.value = remote
            }
        }
    }

    fun product(id: String): Flow<Product?> = products.map { list -> list.find { it.id == id } }

    val lowStockProducts: Flow<List<Product>> = products.map { list ->
        list.filter { it.needsRestock }
    }

    /** Devuelve null si salió bien, o el mensaje de error. Con API, el cambio se guarda en el servidor (admin). */
    suspend fun updateStock(productId: String, stock: Int): String? {
        if (config.isConfigured) {
            val failure = apiCall { api.updateStock(productId, StockRequest(stock)) }.exceptionOrNull()
            if (failure != null) {
                return if ((failure as? ApiException)?.isUnauthorized == true) "Tu sesión venció. Inicia sesión de nuevo."
                else failure.message
            }
        }
        _products.update { list ->
            list.map { if (it.id == productId) it.copy(stock = stock) else it }
        }
        return null
    }

    fun decreaseStock(productId: String, quantity: Int) {
        _products.update { list ->
            list.map { if (it.id == productId) it.copy(stock = (it.stock - quantity).coerceAtLeast(0)) else it }
        }
    }

    private fun loadProducts(): List<Product> {
        val json = context.assets.open(CATALOG_FILE).bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Product>>() {}.type
        return Gson().fromJson(json, type)
    }

    private companion object {
        const val CATALOG_FILE = "catalog.json"
    }
}
