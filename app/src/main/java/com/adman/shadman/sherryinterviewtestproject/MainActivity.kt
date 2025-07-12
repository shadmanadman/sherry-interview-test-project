package com.adman.shadman.sherryinterviewtestproject

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.adman.shadman.sherryinterviewtestproject.ui.theme.SherryInterviewTestProjectTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationPermissionsGranted by mutableStateOf(false)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            locationPermissionsGranted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            if (!locationPermissionsGranted) {
                Log.w("MainActivity", "Location permissions denied by user.")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermissions()

        enableEdgeToEdge()
        setContent {
            SherryInterviewTestProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MapScreen(innerPadding,locationPermissionsGranted,fusedLocationClient)
                }
            }
        }
    }


    private fun checkLocationPermissions() {
        locationPermissionsGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

        if (!locationPermissionsGranted) {
            // Request permissions if not granted
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}


@Composable
fun MapScreen(
    innersPadding: PaddingValues, locationPermissionsGranted: Boolean,
    fusedLocationClient: FusedLocationProviderClient
) {
    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }

    ShowCurrentLocation(locationPermissionsGranted,fusedLocationClient,cameraPositionState)
    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .padding(innersPadding),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true,
            myLocationButtonEnabled = locationPermissionsGranted
        ),
        properties = com.google.maps.android.compose.MapProperties(
            isMyLocationEnabled = locationPermissionsGranted // Enable blue dot
        ),
        onMapLoaded = {
            // Map is loaded and ready for interaction (e.g., adding markers dynamically)
        }
    ) {
    }
}


@Composable
fun ShowCurrentLocation(
    locationPermissionsGranted: Boolean,
    fusedLocationClient: FusedLocationProviderClient, cameraPositionState: CameraPositionState
) {
    // Effect to observe lifecycle and handle initial location logic
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

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
