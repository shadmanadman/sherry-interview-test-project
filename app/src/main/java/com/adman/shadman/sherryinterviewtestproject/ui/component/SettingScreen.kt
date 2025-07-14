package com.adman.shadman.sherryinterviewtestproject.ui.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(onDismiss:()->Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss) {

    }
}