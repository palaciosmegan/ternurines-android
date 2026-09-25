package com.example.testapp.data

import com.example.testapp.data.model.SupportRequest
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class SupportRepository @Inject constructor() {

    private val _requests = MutableStateFlow<List<SupportRequest>>(emptyList())
    val requests: StateFlow<List<SupportRequest>> = _requests.asStateFlow()

    private var nextId = 1

    fun submitRequest(email: String, subject: String, message: String): SupportRequest {
        val request = SupportRequest(
            id = nextId++,
            email = email,
            subject = subject,
            message = message
        )
        _requests.update { it + request }
        return request
    }
}
