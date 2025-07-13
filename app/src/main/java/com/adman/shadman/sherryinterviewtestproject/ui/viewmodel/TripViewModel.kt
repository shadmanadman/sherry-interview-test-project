package com.adman.shadman.sherryinterviewtestproject.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.adman.shadman.sherryinterviewtestproject.data.model.Trip
import com.adman.shadman.sherryinterviewtestproject.di.TripUseCases
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class TripViewModel(private val useCases: TripUseCases) : ViewModel() {

    val allFoods: LiveData<List<Trip>> = useCases.allTripsUseCase.execute()
        .asLiveData(viewModelScope.coroutineContext)

    fun addTrip(startTime: Long, endTime: Long, distance: List<LatLng>) {
        viewModelScope.launch {
            useCases.addTripUseCase.execute(startTime, endTime, distance)
        }
    }

    fun deleteTrip(tripId: Int) {
        viewModelScope.launch {
            useCases.deleteTripUseCase.execute(tripId)
        }
    }
}