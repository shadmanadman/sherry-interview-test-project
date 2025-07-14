package com.adman.shadman.sherryinterviewtestproject.di

import android.content.Context
import androidx.room.Room
import com.adman.shadman.sherryinterviewtestproject.data.db.TripDatabase
import com.adman.shadman.sherryinterviewtestproject.data.db.TripDao
import com.adman.shadman.sherryinterviewtestproject.data.repo.TripRepoImpl
import com.adman.shadman.sherryinterviewtestproject.domain.usecase.AddTripUseCase
import com.adman.shadman.sherryinterviewtestproject.domain.usecase.AllTripsUseCase
import com.adman.shadman.sherryinterviewtestproject.domain.usecase.DeleteTripUseCase

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


    fun initialize(context: Context) {
        // Initialize the database
        database = TripDatabase.getDatabase(context)

        // Initialize DAOs
        tripDao = database.tripDao()

        // Initialize Repositories
        repository = TripRepoImpl(tripDao)

        // Initialize Use Cases
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