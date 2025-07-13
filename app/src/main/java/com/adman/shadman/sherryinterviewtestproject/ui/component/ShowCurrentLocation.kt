package com.adman.shadman.sherryinterviewtestproject.ui.component

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState

@Composable
fun ShowCurrentLocation(
    locationPermissionsGranted: Boolean,
    fusedLocationClient: FusedLocationProviderClient, cameraPositionState: CameraPositionState
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner.lifecycle, locationPermissionsGranted) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START && locationPermissionsGranted) {
                // When the app starts and permissions are granted, try to get last known location
                try {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            val userLatLng = LatLng(it.latitude, it.longitude)
                            cameraPositionState.move(
                                CameraUpdateFactory.newLatLngZoom(
                                    userLatLng,
                                    18f
                                )
                            )
                        } ?: run {
                            Log.w("MapScreen", "Last known location is null.")
                        }
                    }.addOnFailureListener { e ->
                        Log.e("MapScreen", "Failed to get last location: ${e.message}")
                    }
                } catch (e: SecurityException) {
                    Log.e("MapScreen", "Location permission missing: ${e.message}")
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
