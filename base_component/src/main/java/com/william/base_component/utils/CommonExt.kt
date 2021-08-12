package com.william.base_component.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 *  author : WilliamYang
 *  date : 2021/8/12 17:57
 *  description :
 */

val Context.dataStore by preferencesDataStore("easyKt_prefs")

suspend fun <T> DataStore<Preferences>.put(key: Preferences.Key<T>, value: T) {
    edit { preferences ->
        preferences[key] = value
    }
}

fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>, defaultVal: T): Flow<T> {
    return data.catch { exception ->
        // dataStore.data throws an IOException when an error is encountered when reading data
        if (exception is IOException) {
            "Error reading preferences, ${exception.message}".logE()
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[key] ?: defaultVal
    }
}