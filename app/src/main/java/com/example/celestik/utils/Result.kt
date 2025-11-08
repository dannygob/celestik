package com.example.celestik.utils

/**
 * Represents the result of an operation, typically used for async or stateful flows.
 *
 * @param T The type of data returned on success.
 */
sealed class Result<out T> {

    /**
     * Represents a successful result containing data.
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * Represents a failed result containing an exception.
     */
    data class Error(val exception: Exception) : Result<Nothing>()

    /**
     * Represents a loading state.
     */
    object Loading : Result<Nothing>()
}
