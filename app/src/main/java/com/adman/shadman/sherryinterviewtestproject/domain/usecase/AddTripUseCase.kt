package com.adman.shadman.sherryinterviewtestproject.domain.usecase

import com.adman.shadman.sherryinterviewtestproject.data.model.Trip
import com.adman.shadman.sherryinterviewtestproject.domain.TripRepo

class AddTripUseCase(private val tripRepo: TripRepo) {
    suspend fun execute(trip: Trip) = tripRepo.insert(trip)
}