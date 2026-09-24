package com.example.testapp.util

import java.util.Locale

fun formatPrice(amount: Double): String = String.format(Locale.US, "S/ %.2f", amount)
