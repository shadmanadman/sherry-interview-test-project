package com.adman.shadman.sherryinterviewtestproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adman.shadman.sherryinterviewtestproject.data.datastore.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val interval: Int = 1,
    val backgroundTracking: Boolean = true
)

class SettingViewModel(private val settingsDataStore: SettingsDataStore) : ViewModel() {
    val uiState: StateFlow<SettingsUiState> =
        combine(
            settingsDataStore.selectedInterval,
            settingsDataStore.backgroundTracking
        ) { interval, tracking ->
            SettingsUiState(interval, tracking)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState()
        )

    fun updateInterval(newSeconds: Int) {
        viewModelScope.launch { settingsDataStore.setSelectedInterval(newSeconds) }
    }

    fun setBackgroundTracking(enabled: Boolean) {
        viewModelScope.launch { settingsDataStore.setBackgroundTracking(enabled) }
    }
}