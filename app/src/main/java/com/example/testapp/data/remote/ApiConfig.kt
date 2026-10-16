package com.example.testapp.data.remote

import javax.inject.Inject
import javax.inject.Singleton

private const val API_BASE_URL = "https://cavcnncut7.execute-api.us-east-1.amazonaws.com"

@Singleton
class ApiConfig @Inject constructor() {
    val baseUrl: String = API_BASE_URL
    val isConfigured: Boolean = baseUrl.startsWith("http")
}
