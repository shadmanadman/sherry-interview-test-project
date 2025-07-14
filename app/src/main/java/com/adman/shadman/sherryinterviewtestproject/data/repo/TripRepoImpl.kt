package com.adman.shadman.sherryinterviewtestproject.data.repo

import com.adman.shadman.sherryinterviewtestproject.data.db.TripDao
import com.adman.shadman.sherryinterviewtestproject.data.model.Trip
import com.adman.shadman.sherryinterviewtestproject.domain.repo.TripRepo
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

class TripRepoImpl(private val tripDao: TripDao): TripRepo {
    override suspend fun addTrip(
        startTime: Long,
        endTime: Long,
        distance: Long
    ) {
        tripDao.insert(Trip(startTime = startTime, endTime = endTime, distance = distance))
    }

    override suspend fun removeTrip(tripId: Int) {
        tripDao.delete(tripId)
    }

    override fun allTrips(): Flow<List<Trip>>  = tripDao.getAllTrips()
}