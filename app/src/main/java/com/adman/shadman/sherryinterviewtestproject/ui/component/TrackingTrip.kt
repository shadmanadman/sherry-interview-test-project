package com.adman.shadman.sherryinterviewtestproject.ui.component

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.adman.shadman.sherryinterviewtestproject.R
import com.adman.shadman.sherryinterviewtestproject.ui.theme.Typography
import com.adman.shadman.sherryinterviewtestproject.ui.viewmodel.SettingViewModel
import com.adman.shadman.sherryinterviewtestproject.ui.viewmodel.TripViewModel
import java.nio.file.WatchEvent
import java.sql.Time
import java.time.Instant
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrackingTrips(
    tripViewModel: TripViewModel,
    settingViewModel: SettingViewModel,
    locationTracker: LocationTracker,
    modifier: Modifier
) {
    val currentMetrics by locationTracker.metrics
    val context = LocalContext.current

    val startOfTripTime = remember { mutableLongStateOf(Instant.now().toEpochMilli()) }
    val endOfTripTime = remember { mutableLongStateOf(Instant.now().toEpochMilli()) }

    var hasUserStartedTracking by remember { mutableStateOf(false) }
    var showUserTrips by remember { mutableStateOf(false) }
    var showSetting by remember { mutableStateOf(false) }

    if (showUserTrips)
        AllTripScreen(
            tripViewModel,
            onDismiss = {
                showUserTrips = false
            })

    if (showSetting)
        SettingScreen(settingViewModel = settingViewModel, onDismiss = {
            showSetting = false
        })

    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(
                modifier = Modifier
                    .clickable(onClick = {
                        showUserTrips = true
                    })
                    .size(110.dp, 40.dp)
                    .weight(1f)
                    .clip(shape = RoundedCornerShape(22.dp))
                    .background(Color.Green),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "My trips", style = Typography.bodyLarge
                )
            }
            Box(
                modifier = Modifier
                    .clickable(onClick = {
                        showSetting = true
                    })
                    .size(110.dp, 40.dp)
                    .clip(shape = RoundedCornerShape(22.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Setting", style = Typography.bodyLarge, color = Color.White
                )
            }

        }


        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(230.dp),
            shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Trip Tracking", style = Typography.titleLarge
                    )
                }


                // Speed
                Row(modifier = Modifier.padding(start = 12.dp, bottom = 12.dp)) {
                    Text(
                        text = "Speed:", style = Typography.bodyLarge
                    )
                    Text(
                        text = currentMetrics.speedKmh.toString(),
                        style = Typography.titleMedium,
                        color = Color.Green
                    )
                }
                // Distance traveled
                Row(modifier = Modifier.padding(start = 12.dp, bottom = 12.dp)) {
                    Text(
                        text = "Distance:", style = Typography.bodyLarge
                    )
                    Text(
                        text = (currentMetrics.distanceMeters / 1000).toString(),
                        style = Typography.titleMedium,
                        color = Color.Green
                    )
                }

                // Elapsed time
                Row(modifier = Modifier.padding(start = 12.dp, bottom = 12.dp)) {
                    Text(
                        text = "Elapsed time:", style = Typography.bodyLarge
                    )
                    Text(
                        text = formatElapsedTime(currentMetrics.elapsedTimeMillis),
                        style = Typography.titleMedium,
                        color = Color.Green
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceAround
            ) {
                IconButton(onClick = {
                    if (hasUserStartedTracking) {
                        endOfTripTime.longValue = Instant.now().toEpochMilli()
                        tripViewModel.addTrip(
                            startTime = startOfTripTime.longValue,
                            endTime = endOfTripTime.longValue,
                            distance = ((currentMetrics.distanceMeters / 1000).toLong())
                        )
                        locationTracker.stopTracking()
                        Toast.makeText(
                            context,
                            "Tracking Stopped! Your trip has been saved.",
                            Toast.LENGTH_LONG
                        ).show()
                        hasUserStartedTracking = false
                    } else
                        Toast.makeText(
                            context,
                            "No tracking has been started. Please Start one.",
                            Toast.LENGTH_LONG
                        ).show()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.stop), contentDescription = "Stop",
                        tint = Color.Unspecified
                    )
                }
                IconButton(onClick = {
                    startOfTripTime.longValue = Instant.now().toEpochMilli()
                    hasUserStartedTracking = true
                    locationTracker.startTracking()
                    Toast.makeText(context, "Tracking Started!", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.play), contentDescription = "Start",
                        tint = Color.Unspecified
                    )
                }
                IconButton(onClick = {
                    if (hasUserStartedTracking) {
                        locationTracker.pauseTracking()
                        Toast.makeText(context, "Tracking Paused!", Toast.LENGTH_SHORT).show()
                    } else
                        Toast.makeText(
                            context,
                            "No tracking has been started. Please Start one.",
                            Toast.LENGTH_LONG
                        ).show()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.pause),
                        contentDescription = "Pause",
                        tint = Color.Unspecified
                    )
                }
            }
        }

    }
}

fun formatElapsedTime(millis: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(millis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
