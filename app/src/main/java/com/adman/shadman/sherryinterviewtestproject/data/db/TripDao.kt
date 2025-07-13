package com.adman.shadman.sherryinterviewtestproject.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.adman.shadman.sherryinterviewtestproject.data.model.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Insert
    suspend fun insert(trip: Trip)
    @Query("DELETE FROM trip WHERE id = :tripId")
    suspend fun delete(tripId: Int)
    @Query("SELECT * FROM trip")
    fun getAllTrips(): Flow<List<Trip>>
}