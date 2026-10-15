package com.example.testapp.data.remote

import com.example.testapp.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

/** Dice si la app habla con el API de AWS o funciona en modo local (datos en memoria). */
@Singleton
class ApiConfig @Inject constructor() {
    val baseUrl: String = BuildConfig.API_BASE_URL.trim()
    val isConfigured: Boolean = baseUrl.startsWith("http")
}
