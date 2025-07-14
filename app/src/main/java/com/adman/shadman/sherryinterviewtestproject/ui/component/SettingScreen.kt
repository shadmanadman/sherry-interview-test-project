package com.adman.shadman.sherryinterviewtestproject.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adman.shadman.sherryinterviewtestproject.ui.viewmodel.SettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(settingViewModel: SettingViewModel,onDismiss:()->Unit) {
    val state by settingViewModel.uiState.collectAsState()

    ModalBottomSheet(onDismissRequest = onDismiss) {

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Interval (sec): ${state.interval}")
            Slider(
                value = state.interval.toFloat(),
                onValueChange = { settingViewModel.updateInterval(it.toInt()) },
                valueRange = 0.5f..5f
            )

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Background tracking")
                Switch(
                    checked = state.backgroundTracking,
                    onCheckedChange = settingViewModel::setBackgroundTracking
                )
            }
        }
    }
}