package com.ask.technologies.campaign.helpers

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore(name = "ask_fcm")

class DataStoreHelper(private val context: Context) {

    // Save a String value
    fun saveString(key: String, value: String) = runBlocking {
        val dataKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataKey] = value
        }
    }

    // Get a String value
    fun getString(key: String, defaultValue: String = ""): String = runBlocking {
        val dataKey = stringPreferencesKey(key)
        context.dataStore.data
            .map { preferences -> preferences[dataKey] ?: defaultValue }
            .first()
    }

    // Save an Int value
    fun saveInt(key: String, value: Int) = runBlocking {
        val dataKey = intPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataKey] = value
        }
    }

    // Get an Int value
    fun getInt(key: String, defaultValue: Int = 0): Int = runBlocking {
        val dataKey = intPreferencesKey(key)
        context.dataStore.data
            .map { preferences -> preferences[dataKey] ?: defaultValue }
            .first()
    }

    // Save a Boolean value
    fun saveBoolean(key: String, value: Boolean) = runBlocking {
        val dataKey = booleanPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataKey] = value
        }
    }

    // Get a Boolean value
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean = runBlocking {
        val dataKey = booleanPreferencesKey(key)
        context.dataStore.data
            .map { preferences -> preferences[dataKey] ?: defaultValue }
            .first()
    }

    // Save a Float value
    fun saveFloat(key: String, value: Float) = runBlocking {
        val dataKey = floatPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataKey] = value
        }
    }

    // Get a Float value
    fun getFloat(key: String, defaultValue: Float = 0f): Float = runBlocking {
        val dataKey = floatPreferencesKey(key)
        context.dataStore.data
            .map { preferences -> preferences[dataKey] ?: defaultValue }
            .first()
    }

    // Save a Long value
    fun saveLong(key: String, value: Long) = runBlocking {
        val dataKey = longPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataKey] = value
        }
    }

    // Get a Long value
    fun getLong(key: String, defaultValue: Long = 0L): Long = runBlocking {
        val dataKey = longPreferencesKey(key)
        context.dataStore.data
            .map { preferences -> preferences[dataKey] ?: defaultValue }
            .first()
    }



    // Clear all preferences
    fun clearAll() = runBlocking {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
