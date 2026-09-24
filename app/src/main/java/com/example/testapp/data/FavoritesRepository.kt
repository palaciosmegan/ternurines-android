package com.example.testapp.data

import android.content.Context
import androidx.core.content.edit
import com.example.testapp.data.model.Product
import com.example.testapp.data.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor(
    @ApplicationContext context: Context,
    catalogRepository: CatalogRepository,
    private val authRepository: AuthRepository
) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val favoritesByUser = MutableStateFlow<Map<String, Set<String>>>(emptyMap())

    val favoriteIds: Flow<Set<String>> = combine(authRepository.currentUser, favoritesByUser) { user, cache ->
        val key = keyFor(user)
        cache[key] ?: read(key)
    }

    val favorites: Flow<List<Product>> = combine(catalogRepository.products, favoriteIds) { products, ids ->
        products.filter { it.id in ids }
    }

    fun toggle(productId: String) {
        val key = keyFor(authRepository.currentUser.value)
        val current = favoritesByUser.value[key] ?: read(key)
        val updated = if (productId in current) current - productId else current + productId

        prefs.edit { putStringSet(key, updated) }
        favoritesByUser.update { it + (key to updated) }
    }

    private fun read(key: String): Set<String> = prefs.getStringSet(key, emptySet()).orEmpty().toSet()

    private fun keyFor(user: User?): String = "favorites_${user?.email?.lowercase() ?: GUEST}"

    private companion object {
        const val PREFS_NAME = "favorites"
        const val GUEST = "guest"
    }
}
