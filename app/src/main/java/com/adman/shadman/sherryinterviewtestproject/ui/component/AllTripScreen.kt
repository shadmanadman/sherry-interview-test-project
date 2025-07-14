package com.adman.shadman.sherryinterviewtestproject.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import com.adman.shadman.sherryinterviewtestproject.ui.theme.Typography
import com.adman.shadman.sherryinterviewtestproject.ui.viewmodel.TripViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTripScreen(tripViewModel: TripViewModel, onDismiss: () -> Unit) {
    val allTrips by tripViewModel.allTrips.observeAsState()
    ModalBottomSheet(onDismissRequest = onDismiss) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            if (allTrips.isNullOrEmpty().not()) {
                items(allTrips?.size ?: 0) { index ->
                    Column {
                        Text(
                            text = "Trip ${index.plus(1)}",
                            style = Typography.titleLarge.copy(Color.Green),
                        )
                        Text(
                            text = "Started in: ${allTrips?.get(index)?.startTime?.toFormattedDateTime() ?: ""}",
                            style = Typography.titleMedium.copy(Color.Black),
                            modifier = Modifier
                                .padding(8.dp)
                        )

                        Text(
                            text = "Ended in: ${allTrips?.get(index)?.endTime?.toFormattedDateTime() ?: ""}",
                            style = Typography.titleMedium.copy(Color.Black),
                            modifier = Modifier
                                .padding(8.dp)
                        )

                        Text(
                            text = "Distance: ${allTrips?.get(index)?.distance}KM",
                            style = Typography.titleMedium.copy(Color.Black),
                            modifier = Modifier
                                .padding(8.dp)
                        )

                        VerticalDivider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp)
                    }

                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun Long.toFormattedDateTime(
    pattern: String = "EE, MM, dd, yy HH:mm:ss",
    zoneId: ZoneId = ZoneId.systemDefault(),
    locale: Locale = Locale.getDefault()
): String {
    val instant = Instant.ofEpochMilli(this)
    val zonedDateTime = instant.atZone(zoneId)
    val formatter = DateTimeFormatter.ofPattern(pattern, locale)
    return formatter.format(zonedDateTime)
}