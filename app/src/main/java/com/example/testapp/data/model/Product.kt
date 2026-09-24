package com.example.testapp.data.model

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val imageAlt: String,
    val stock: Int,
    val category: String,
    val subcategory: String? = null,
    val ageMin: Int? = null,
    val pieces: Int? = null,
    val featured: Boolean = false
) {
    val isOutOfStock: Boolean get() = stock <= 0
    val isLowStock: Boolean get() = stock in 1 until LOW_STOCK_THRESHOLD
    val isFewLeft: Boolean get() = stock in 1..FEW_LEFT_THRESHOLD

    companion object {
        const val LOW_STOCK_THRESHOLD = 10
        const val FEW_LEFT_THRESHOLD = 5
    }
}
