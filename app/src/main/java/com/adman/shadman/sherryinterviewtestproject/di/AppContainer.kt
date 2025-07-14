package com.adman.shadman.sherryinterviewtestproject.di

import android.content.Context
import androidx.room.Room
import com.adman.shadman.sherryinterviewtestproject.data.db.TripDatabase
import com.adman.shadman.sherryinterviewtestproject.data.db.TripDao
import com.adman.shadman.sherryinterviewtestproject.data.repo.TripRepoImpl
import com.adman.shadman.sherryinterviewtestproject.domain.usecase.AddTripUseCase
import com.adman.shadman.sherryinterviewtestproject.domain.usecase.AllTripsUseCase
import com.adman.shadman.sherryinterviewtestproject.domain.usecase.DeleteTripUseCase
import com.adman.shadman.sherryinterviewtestproject.ui.component.LocationTracker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

/**
 * I created a custom di to present my deep knowledge of dependency injections.
 */
object AppContainer {
    // Database instance
    private lateinit var database: TripDatabase

    // DAOs
    private lateinit var tripDao: TripDao

    // Repositories
    private lateinit var repository: TripRepoImpl

    // Use Cases
    lateinit var useCases: TripUseCases
        private set

    // Location tracker
    lateinit var locationTracker: LocationTracker
    lateinit var fusedClient: FusedLocationProviderClient


    fun initialize(context: Context) {
        // Create a global instance for our location tracker to inject in background service.
        fusedClient = LocationServices.getFusedLocationProviderClient(context)
        locationTracker = LocationTracker(fusedClient)

        database = TripDatabase.getDatabase(context)

        tripDao = database.tripDao()

        repository = TripRepoImpl(tripDao)

        useCases = TripUseCases(
            addTripUseCase = AddTripUseCase(repository),
            deleteTripUseCase = DeleteTripUseCase(repository),
            allTripsUseCase = AllTripsUseCase(repository)
        )
    }
}

class TripUseCases(
    val addTripUseCase: AddTripUseCase,
    val deleteTripUseCase: DeleteTripUseCase,
    val allTripsUseCase: AllTripsUseCase
)