package com.adman.shadman.sherryinterviewtestproject.domain.usecase

import com.adman.shadman.sherryinterviewtestproject.domain.TripRepo

class AllTripsUseCase(private val tripRepo: TripRepo) {
    fun execute() = tripRepo.getAllTrips()
}