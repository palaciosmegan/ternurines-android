package com.example.testapp.data

import android.content.Context
import com.example.testapp.data.model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val _products = MutableStateFlow(loadProducts())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    fun product(id: String): Flow<Product?> = products.map { list -> list.find { it.id == id } }

    private fun loadProducts(): List<Product> {
        val json = context.assets.open(CATALOG_FILE).bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Product>>() {}.type
        return Gson().fromJson(json, type)
    }

    private companion object {
        const val CATALOG_FILE = "catalog.json"
    }
}
