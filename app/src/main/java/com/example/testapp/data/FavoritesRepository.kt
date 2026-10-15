package com.example.testapp.data

import android.content.Context
import androidx.core.content.edit
import com.example.testapp.data.model.Product
import com.example.testapp.data.model.User
import com.example.testapp.data.remote.ApiConfig
import com.example.testapp.data.remote.ApiException
import com.example.testapp.data.remote.ProductRefRequest
import com.example.testapp.data.remote.TernurinesApi
import com.example.testapp.data.remote.apiCall
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Favoritos por usuario, guardados en el teléfono. Con API y sesión iniciada también se sincronizan
 * con el servidor: se bajan al iniciar sesión y cada cambio se envía en segundo plano.
 */
@Singleton
class FavoritesRepository @Inject constructor(
    @ApplicationContext context: Context,
    catalogRepository: CatalogRepository,
    private val authRepository: AuthRepository,
    private val api: TernurinesApi,
    private val config: ApiConfig
) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val favoritesByUser = MutableStateFlow<Map<String, Set<String>>>(emptyMap())
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val favoriteIds: Flow<Set<String>> = combine(authRepository.currentUser, favoritesByUser) { user, cache ->
        val key = keyFor(user)
        cache[key] ?: read(key)
    }

    val favorites: Flow<List<Product>> = combine(catalogRepository.products, favoriteIds) { products, ids ->
        products.filter { it.id in ids }
    }

    init {
        if (config.isConfigured) {
            scope.launch {
                // Se ejecuta al abrir la app con sesión guardada y cada vez que alguien inicia sesión.
                authRepository.currentUser.collect { user -> if (user != null) pullRemote(user) }
            }
        }
    }

    fun toggle(productId: String) {
        val user = authRepository.currentUser.value
        val key = keyFor(user)
        val current = favoritesByUser.value[key] ?: read(key)
        val adding = productId !in current
        val updated = if (adding) current + productId else current - productId

        prefs.edit { putStringSet(key, updated) }
        favoritesByUser.update { it + (key to updated) }

        if (user != null) pushRemote(user, productId, adding)
    }

    private fun pushRemote(user: User, productId: String, adding: Boolean) {
        if (!config.isConfigured) return
        scope.launch {
            val result = apiCall {
                if (adding) api.addFavorite(ProductRefRequest(productId)) else api.removeFavorite(productId)
            }
            result.onFailure { error ->
                if ((error as? ApiException)?.code == 409) return@onFailure // ya estaba como favorito
                pullRemote(user) // si falló, se vuelve a lo que dice el servidor
            }
        }
    }

    private suspend fun pullRemote(user: User) {
        apiCall { api.favorites() }
            .onSuccess { list ->
                val ids = list.map { it.product.id }.toSet()
                val key = keyFor(user)
                prefs.edit { putStringSet(key, ids) }
                favoritesByUser.update { it + (key to ids) }
            }
            .onFailure { error ->
                if ((error as? ApiException)?.isUnauthorized == true) authRepository.expireSession()
            }
    }

    private fun read(key: String): Set<String> = prefs.getStringSet(key, emptySet()).orEmpty().toSet()

    private fun keyFor(user: User?): String = "favorites_${user?.email?.lowercase() ?: GUEST}"

    private companion object {
        const val PREFS_NAME = "favorites"
        const val GUEST = "guest"
    }
}
