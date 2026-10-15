package com.example.testapp.data.remote

import com.google.gson.Gson
import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.io.IOException

class ApiException(message: String, val code: Int? = null) : Exception(message) {
    val isUnauthorized: Boolean get() = code == 401
}

/** Ejecuta una llamada al API y convierte los errores en mensajes amigables para el usuario. */
suspend fun <T> apiCall(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (e: HttpException) {
    Result.failure(ApiException(serverMessage(e), e.code()))
} catch (e: IOException) {
    Result.failure(ApiException("No pudimos conectarnos. Revisa tu internet e inténtalo de nuevo."))
} catch (e: JsonParseException) {
    Result.failure(ApiException("Recibimos una respuesta inesperada del servidor."))
}

private fun serverMessage(e: HttpException): String {
    val fromBody = try {
        e.response()?.errorBody()?.string()?.let { Gson().fromJson(it, ErrorResponse::class.java)?.error }
    } catch (ignored: JsonParseException) {
        null
    }
    return fromBody?.takeIf { it.isNotBlank() } ?: "Algo salió mal (código ${e.code()}). Inténtalo de nuevo."
}
