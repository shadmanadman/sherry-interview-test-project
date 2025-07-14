package com.adman.shadman.sherryinterviewtestproject.ui.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.adman.shadman.sherryinterviewtestproject.R
import com.adman.shadman.sherryinterviewtestproject.di.AppContainer
import com.adman.shadman.sherryinterviewtestproject.ui.component.LocationTracker

const val INTERVAL = "interval"
const val CHANNEL_ID = "location_tracking"
class LocationTrackingService : Service() {
    private val tracker: LocationTracker by lazy {
        AppContainer.locationTracker
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        val interval = intent?.getLongExtra(INTERVAL, 1000L) ?: 1000L
        tracker.startTracking(interval)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        tracker.stopTracking()
    }

    private fun createNotificationChannel() {
        val channelName = "Location Tracking Service"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val chan = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
                description = "Notifications for active location tracking service"
                setShowBadge(false)
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(chan)
        }
    }

    private fun createNotification(): Notification {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Tracking your location")
            .setContentText("Location tracking is running in background.")
            .setSmallIcon(R.drawable.play)
            .setOngoing(true)

        return notificationBuilder.build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val NOTIFICATION_ID = 1001
    }
}