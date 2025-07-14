package com.adman.shadman.sherryinterviewtestproject.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val SETTINGS_NAME = "app_settings"
private val Context.dataStore by preferencesDataStore(SETTINGS_NAME)

object SettingsKeys {
    val SELECTED_INTERVAL = intPreferencesKey("selected_interval")
    val BACKGROUND_TRACKING = booleanPreferencesKey("background_tracking")
}

class SettingsDataStore(private val context: Context) {

    val selectedInterval: Flow<Int> =
        context.dataStore.data.map { prefs ->
            prefs[SettingsKeys.SELECTED_INTERVAL] ?: 60
        }

    val backgroundTracking: Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[SettingsKeys.BACKGROUND_TRACKING] ?: false
        }

    suspend fun setSelectedInterval(seconds: Int) {
        context.dataStore.edit { it[SettingsKeys.SELECTED_INTERVAL] = seconds }
    }

    suspend fun setBackgroundTracking(enabled: Boolean) {
        context.dataStore.edit { it[SettingsKeys.BACKGROUND_TRACKING] = enabled }
    }
}