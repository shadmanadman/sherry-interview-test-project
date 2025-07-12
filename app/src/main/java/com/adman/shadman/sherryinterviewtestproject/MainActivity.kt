package com.adman.shadman.sherryinterviewtestproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.adman.shadman.sherryinterviewtestproject.ui.theme.SherryInterviewTestProjectTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SherryInterviewTestProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MapScreen(innerPadding)
                }
            }
        }
    }
}



@Composable
fun MapScreen(innersPadding: PaddingValues) {
    val singapore = LatLng(1.35, 103.87) // Example: Singapore coordinates
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f) // Set initial camera position and zoom
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize().padding(innersPadding),
        cameraPositionState = cameraPositionState,
        // Optional: Customize map properties and UI settings
        // properties = MapProperties(mapType = MapType.HYBRID, isTrafficEnabled = true),
        // uiSettings = MapUiSettings(zoomControlsEnabled = true, myLocationButtonEnabled = true),
        onMapLoaded = {
            // Map is loaded and ready for interaction (e.g., adding markers dynamically)
        }
    ) {
        // Content for the map: markers, polylines, etc.
//        Marker(
//            state = MarkerState(position = singapore),
//            title = "Marker in Singapore",
//            snippet = "Some details about Singapore"
//        )

        // You can add more markers, polylines, polygons here
        // val sydney = LatLng(-34.0, 151.0)
        // Marker(
        //    state = MarkerState(position = sydney),
        //    title = "Marker in Sydney"
        // )
    }
}
