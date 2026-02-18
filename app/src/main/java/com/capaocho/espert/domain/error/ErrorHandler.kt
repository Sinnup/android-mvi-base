package com.capaocho.espert.domain.error

interface ErrorHandler {
    fun getError(throwable: Throwable): String
}