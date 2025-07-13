package com.adman.shadman.sherryinterviewtestproject.domain.usecase

import com.adman.shadman.sherryinterviewtestproject.data.db.TripDao
import com.adman.shadman.sherryinterviewtestproject.domain.repo.TripRepo

class AllTripsUseCase(private val tripRepo: TripRepo) {
     fun execute() = tripRepo.allTrips()
}