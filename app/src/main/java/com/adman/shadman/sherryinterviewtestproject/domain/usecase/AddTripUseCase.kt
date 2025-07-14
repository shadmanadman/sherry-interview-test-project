package com.adman.shadman.sherryinterviewtestproject.domain.usecase

import com.adman.shadman.sherryinterviewtestproject.domain.repo.TripRepo

class AddTripUseCase(private val tripRepo: TripRepo) {
    suspend fun execute(startTime: Long, endTime: Long, distance: Long) =
        tripRepo.addTrip(startTime = startTime, endTime, distance)
}