package com.adman.shadman.sherryinterviewtestproject.domain.usecase

import com.adman.shadman.sherryinterviewtestproject.data.model.Trip
import com.adman.shadman.sherryinterviewtestproject.data.db.TripDao
import com.adman.shadman.sherryinterviewtestproject.domain.repo.TripRepo
import com.google.android.gms.maps.model.LatLng

class AddTripUseCase(private val tripRepo: TripRepo) {
    suspend fun execute(startTime: Long, endTime: Long, distance: List<LatLng>) =
        tripRepo.addTrip(startTime = startTime, endTime, distance)
}