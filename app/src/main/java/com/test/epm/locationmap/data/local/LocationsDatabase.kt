package com.test.epm.locationmap.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.test.epm.locationmap.data.local.entity.DefaultLocation
import com.test.epm.locationmap.data.local.entity.UserLocation

@Database(entities = [(UserLocation::class), (DefaultLocation::class)], version = 1, exportSchema = false)
abstract class LocationsDatabase : RoomDatabase() {
    abstract fun locationsDao(): LocationsDao
}