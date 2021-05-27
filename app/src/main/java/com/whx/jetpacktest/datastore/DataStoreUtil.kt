package com.whx.jetpacktest.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.whx.jetpacktest.NBApplication
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.io.IOException

object DataStoreUtil {
    private val Context.mPref by preferencesDataStore(name = "settings")

    private val prefDataStore: DataStore<Preferences> = NBApplication.getAppContext().mPref

    fun getInt(key: String, dft: Int = 0): Flow<Int> {
        return prefDataStore.data.catch { e ->
            if (e is IOException) {
                e.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            it[intPreferencesKey(key)] ?: dft
        }
    }

    fun getIntSync(key: String, dft: Int = 0): Int {
        var value = 0
        runBlocking {
            prefDataStore.data.first {
                value = it[intPreferencesKey(key)] ?: dft
                true
            }
        }
        return value
    }

    fun getString(key: String, dft: String = ""): Flow<String> {
        return prefDataStore.data.catch { e ->
            if (e is IOException) {
                e.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            it[stringPreferencesKey(key)] ?: dft
        }
    }

    fun getStringSync(key: String, dft: String = ""): String {
        var value = dft
        runBlocking {
            prefDataStore.data.first {
                value = it[stringPreferencesKey(key)] ?: dft
                true
            }
        }
        return value
    }

    suspend fun putString(key: String, value: String) {
        prefDataStore.edit { pref -> pref[stringPreferencesKey(key)] = value }
    }

    fun putStringSync(key: String, value: String) {
        runBlocking {
            prefDataStore.edit { pref -> pref[stringPreferencesKey(key)] = value }
        }
    }
}