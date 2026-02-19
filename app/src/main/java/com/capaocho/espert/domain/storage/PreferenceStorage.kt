package com.capaocho.espert.domain.storage

import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the methods for local preference storage.
 *
 * This abstraction allows different implementations for storing simple application
 * preferences, such as Jetpack DataStore or SharedPreferences.
 */
interface PreferenceStorage {

    /**
     * Saves a value associated with the specified key.
     *
     * @param key The key to store the value under.
     * @param value The value to store. Supported types should include basic primitives and [String].
     */
    suspend fun <T> savePreference(key: String, value: T)

    /**
     * Retrieves a preference value as a [Flow].
     *
     * If the value is not found, the [defaultValue] is returned.
     *
     * @param key The unique identifier for the preference.
     * @param defaultValue The value to return if the preference is missing.
     * @return A [Flow] emitting the current value of the preference.
     */
    fun <T> getPreference(key: String, defaultValue: T): Flow<T>

    /**
     * Clears all stored data from the storage.
     */
    suspend fun clearAll()
}
