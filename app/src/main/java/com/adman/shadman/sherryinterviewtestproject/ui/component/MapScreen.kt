package com.adman.shadman.sherryinterviewtestproject.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    innersPadding: PaddingValues, locationPermissionsGranted: Boolean,
    fusedLocationClient: FusedLocationProviderClient,
    locationTracker: LocationTracker
) {
    val trackingState by locationTracker.trackingState
    val pathPoints = remember { locationTracker.pathPoints }

    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }

    ShowCurrentLocation(locationPermissionsGranted, fusedLocationClient, cameraPositionState)

    // Animate camera to user's last location when tracking starts
    DisposableEffect(trackingState) {
        if (trackingState == TrackingState.TRACKING && locationPermissionsGranted) {
            if (pathPoints.isNotEmpty())
                cameraPositionState.move(update = CameraUpdateFactory.newLatLng(pathPoints.last()))
        }
        onDispose {
            locationTracker.pauseTracking()
        }
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .padding(innersPadding),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = locationPermissionsGranted
        ),
        properties = MapProperties(
            isMyLocationEnabled = locationPermissionsGranted
        )
    ) {
        // Draw the tracking path
        if (pathPoints.isNotEmpty()) {
            Polyline(points = pathPoints, color = MaterialTheme.colorScheme.primary, width = 8f)
        }
    }
}
