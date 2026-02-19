package com.capaocho.espert.domain.common

/**
 * A generic wrapper for results from data operations.
 *
 * This sealed class represents either a successful outcome with data [T] or
 * a failure with an [Exception].
 */
sealed class Result<out T : Any> {
    /**
     * Represents a successful result containing data.
     */
    data class Success<out T : Any>(val data: T) : Result<T>()

    /**
     * Represents a failed result containing an exception.
     */
    data class Error(val exception: Exception) : Result<Nothing>()
}
