package com.example.testapp.data.remote

import com.example.testapp.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiConfig @Inject constructor() {
    val baseUrl: String = BuildConfig.API_BASE_URL.trim()
    val isConfigured: Boolean = baseUrl.startsWith("http")
}
