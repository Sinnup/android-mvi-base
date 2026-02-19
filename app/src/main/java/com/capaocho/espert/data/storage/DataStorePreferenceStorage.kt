package com.capaocho.espert.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.capaocho.espert.domain.storage.PreferenceStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

/**
 * Implementation of [PreferenceStorage] using Jetpack DataStore Preferences.
 *
 * This class provides a thread-safe way to store and retrieve simple key-value pairs
 * using coroutines and flows.
 *
 * @property dataStore The underlying [DataStore] instance used for persistence.
 */
class DataStorePreferenceStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferenceStorage {

    /**
     * Saves a preference value associated with the given key.
     *
     * @param key The unique identifier for the preference.
     * @param value The value to be stored. Supported types: [String], [Int], [Boolean], [Float], [Long].
     * @throws IllegalArgumentException if the type of [value] is not supported.
     */
    override suspend fun <T> savePreference(key: String, value: T) {
        dataStore.edit { preferences ->
            when (value) {
                is String -> preferences[stringPreferencesKey(key)] = value
                is Int -> preferences[intPreferencesKey(key)] = value
                is Boolean -> preferences[booleanPreferencesKey(key)] = value
                is Float -> preferences[floatPreferencesKey(key)] = value
                is Long -> preferences[longPreferencesKey(key)] = value
                else -> throw IllegalArgumentException("Type not supported")
            }
        }
    }

    /**
     * Retrieves a preference value as a [Flow].
     *
     * If the value is not found, the [defaultValue] is returned.
     *
     * @param key The unique identifier for the preference.
     * @param defaultValue The value to return if the preference is missing.
     * @return A [Flow] emitting the current value of the preference.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T> getPreference(key: String, defaultValue: T): Flow<T> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val result = when (defaultValue) {
                    is String -> preferences[stringPreferencesKey(key)]
                    is Int -> preferences[intPreferencesKey(key)]
                    is Boolean -> preferences[booleanPreferencesKey(key)]
                    is Float -> preferences[floatPreferencesKey(key)]
                    is Long -> preferences[longPreferencesKey(key)]
                    else -> throw IllegalArgumentException("Type not supported")
                }
                (result as? T) ?: defaultValue
            }
    }

    /**
     * Clears all stored preferences in this DataStore.
     */
    override suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}
