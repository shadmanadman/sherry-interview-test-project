package com.adman.shadman.sherryinterviewtestproject.domain.repo

import com.adman.shadman.sherryinterviewtestproject.data.model.Trip
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface TripRepo {
    suspend fun addTrip(startTime:Long,endTime:Long,distance: Long)
    suspend fun removeTrip(tripId:Int)
    fun allTrips(): Flow<List<Trip>>
}