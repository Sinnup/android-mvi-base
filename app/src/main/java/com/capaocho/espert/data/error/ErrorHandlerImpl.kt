package com.capaocho.espert.data.error

import com.capaocho.espert.domain.error.ErrorHandler
import io.ktor.client.plugins.ResponseException
import java.io.IOException
import java.net.HttpURLConnection

class ErrorHandlerImpl : ErrorHandler {

    override fun getError(throwable: Throwable): String {
        return when (throwable) {
            is IOException -> "No internet connection"
            is ResponseException -> {
                when (throwable.response.status.value) {
                    HttpURLConnection.HTTP_INTERNAL_ERROR -> "Internal server error"
                    HttpURLConnection.HTTP_NOT_FOUND -> "Resource not found"
                    else -> "An unexpected error occurred"
                }
            }
            else -> "An unexpected error occurred"
        }
    }
}