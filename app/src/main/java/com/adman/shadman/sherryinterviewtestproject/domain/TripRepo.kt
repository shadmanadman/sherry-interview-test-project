package com.adman.shadman.sherryinterviewtestproject.domain

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.adman.shadman.sherryinterviewtestproject.data.model.Trip

@Dao
interface TripRepo {
    @Insert
    suspend fun insert(trip: Trip)
    @Delete
    suspend fun delete(trip: Trip)
    @Query("SELECT * FROM trip")
    fun getAllTrips(): LiveData<List<Trip>>
}