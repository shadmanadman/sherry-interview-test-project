package com.adman.shadman.sherryinterviewtestproject.ui.component

import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.logging.Handler

enum class TrackingState {
    IDLE, TRACKING, PAUSED
}

data class TrackingMetrics(
    val speedKmh: Float = 0f,
    val distanceMeters: Float = 0f,
    val elapsedTimeMillis: Long = 0L
)

class LocationTracker(private val fusedLocationClient: FusedLocationProviderClient) {
    val trackingState = mutableStateOf(TrackingState.IDLE)
    val metrics = mutableStateOf(TrackingMetrics())
    val pathPoints = mutableStateListOf<LatLng>()

    private var lastLocation: Location? = null
    private var startTimeMillis: Long = 0L
    private var totalDistanceMeters = 0f
    private var pausedTimeMillis: Long = 0L

    private val handler = android.os.Handler(Looper.getMainLooper())
    private val updateMetricsRunnable = object : Runnable {
        override fun run() {
            if (trackingState.value == TrackingState.TRACKING) {
                val elapsedTime = System.currentTimeMillis() - startTimeMillis - pausedTimeMillis
                metrics.value = metrics.value.copy(
                    distanceMeters = totalDistanceMeters,
                    elapsedTimeMillis = elapsedTime
                )
                handler.postDelayed(this, 1000)
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { newLocation ->
                if (trackingState.value == TrackingState.TRACKING) {
                    // Update speed
                    val currentSpeedKmh = newLocation.speed * 3.6f // m/s to km/h
                    metrics.value = metrics.value.copy(speedKmh = currentSpeedKmh)

                    // Update distance
                    lastLocation?.let { prevLocation ->
                        val segmentDistance = prevLocation.distanceTo(newLocation)
                        totalDistanceMeters += segmentDistance
                    }
                    lastLocation = newLocation

                    pathPoints.add(LatLng(newLocation.latitude, newLocation.longitude))
                    Log.d("LocationTracker", "New location: ${newLocation.latitude}, ${newLocation.longitude}, Speed: $currentSpeedKmh km/h, Total Distance: $totalDistanceMeters m")
                } else if (trackingState.value == TrackingState.IDLE) {
                    // When idle, only update lastLocation to ensure when tracking starts, we have a reference
                    lastLocation = newLocation
                    Log.d("LocationTracker", "Idle, updating last location for start reference.")
                }
            }
        }
    }

    @Suppress("MissingPermission")
    fun startLocationUpdates(minUpdateInterval:Long) {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, minUpdateInterval) // Update every 5 seconds
            .setMinUpdateIntervalMillis(minUpdateInterval)
            .build()
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        Log.d("LocationTracker", "Location updates started.")
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d("LocationTracker", "Location updates stopped.")
    }


    fun startTracking(timeInterval:Long) {
        if (trackingState.value == TrackingState.IDLE || trackingState.value == TrackingState.PAUSED) {
            if (trackingState.value == TrackingState.IDLE) {
                // Reset for a new track
                totalDistanceMeters = 0f
                metrics.value = TrackingMetrics()
                pathPoints.clear()
                startTimeMillis = System.currentTimeMillis()
                pausedTimeMillis = 0L
            } else if (trackingState.value == TrackingState.PAUSED) {
                // Adjust start time for resume
                startTimeMillis += (System.currentTimeMillis() - (startTimeMillis + metrics.value.elapsedTimeMillis + pausedTimeMillis))
                pausedTimeMillis = 0L
            }
            trackingState.value = TrackingState.TRACKING
            startLocationUpdates(timeInterval)
            handler.post(updateMetricsRunnable)
            Log.d("LocationTracker", "Tracking started/resumed.")
        }
    }


    fun pauseTracking() {
        if (trackingState.value == TrackingState.TRACKING) {
            trackingState.value = TrackingState.PAUSED
            stopLocationUpdates()
            handler.removeCallbacks(updateMetricsRunnable)
            pausedTimeMillis += (System.currentTimeMillis() - (startTimeMillis + metrics.value.elapsedTimeMillis + pausedTimeMillis)) // Accumulate paused time
            Log.d("LocationTracker", "Tracking paused.")
        }
    }

    fun stopTracking() {
        if (trackingState.value != TrackingState.IDLE) {
            trackingState.value = TrackingState.IDLE
            stopLocationUpdates()
            handler.removeCallbacks(updateMetricsRunnable)
             metrics.value = TrackingMetrics()
             pathPoints.clear()
            Log.d("LocationTracker", "Tracking stopped.")
        }
    }
}