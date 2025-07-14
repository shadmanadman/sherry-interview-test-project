package com.adman.shadman.sherryinterviewtestproject.ui.component

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.adman.shadman.sherryinterviewtestproject.data.datastore.SettingsDataStore
import com.adman.shadman.sherryinterviewtestproject.di.AppContainer
import com.adman.shadman.sherryinterviewtestproject.ui.theme.SherryInterviewTestProjectTheme
import com.adman.shadman.sherryinterviewtestproject.ui.viewmodel.SettingViewModel
import com.adman.shadman.sherryinterviewtestproject.ui.viewmodel.TripViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermissions()

        val tripViewModel = TripViewModel(AppContainer.useCases)
        val settingViewModel = SettingViewModel(SettingsDataStore(this))

        setContent {
            SherryInterviewTestProjectTheme {
                val locationTracker = remember { LocationTracker(fusedLocationClient) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        MapScreen(
                            innersPadding = innerPadding,
                            locationPermissionsGranted = locationPermissionsGranted,
                            fusedLocationClient = fusedLocationClient,
                            locationTracker = locationTracker
                        )
                        TrackingTrips(
                            settingViewModel = settingViewModel,
                            tripViewModel = tripViewModel,
                            locationTracker = locationTracker,
                            modifier = Modifier.align(alignment = Alignment.BottomCenter)
                        )
                    }
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




