package com.adman.shadman.sherryinterviewtestproject.domain.usecase

import com.adman.shadman.sherryinterviewtestproject.data.model.Trip
import com.adman.shadman.sherryinterviewtestproject.data.db.TripDao
import com.adman.shadman.sherryinterviewtestproject.domain.repo.TripRepo

class DeleteTripUseCase(private val tripRepo: TripRepo) {
    suspend fun execute(tripId: Int) = tripRepo.removeTrip(tripId)
}