package com.example.testapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatPrice(amount: Double): String = String.format(Locale.US, "S/ %.2f", amount)

private val peruLocale = Locale("es", "PE")

/** Ej.: "vie 16 oct" */
fun formatShortDate(millis: Long): String =
    SimpleDateFormat("EEE d MMM", peruLocale).format(Date(millis)).replace(".", "")
