package com.adman.shadman.sherryinterviewtestproject.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adman.shadman.sherryinterviewtestproject.data.model.Trip
import com.adman.shadman.sherryinterviewtestproject.data.db.TripDao

@Database(entities = [Trip::class], version = 1, exportSchema = false)
abstract class TripDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    companion object Companion {
        @Volatile
        private var INSTANCE: TripDatabase? = null
        fun getDatabase(context: Context): TripDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TripDatabase::class.java,
                    "trip_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}