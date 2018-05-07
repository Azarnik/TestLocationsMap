package com.test.epm.locationmap.data.local

import android.arch.persistence.room.*
import com.test.epm.locationmap.data.local.entity.DefaultLocation
import com.test.epm.locationmap.data.local.entity.UserLocation
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface LocationsDao {
    @Query("SELECT * FROM user_locations")
    fun userLocations(): Flowable<List<UserLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(userLocation: UserLocation)

    @Query("SELECT * FROM default_locations")
    fun defaultLocations(): Flowable<List<DefaultLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllDefaultLocations(listDefaultLocation: List<DefaultLocation>)

    @Query("SELECT updatedAt FROM default_locations LIMIT 1")
    fun getDefaultUpdatedAt(): Long

    @Query("DELETE FROM default_locations")
    fun clearDefaultLocationsTable()

    @Query("SELECT * FROM default_locations WHERE id = :id")
    fun getDefaultLocationById(id: Long): Single<DefaultLocation>

    @Query("SELECT * FROM user_locations WHERE id = :id")
    fun getUserLocationById(id: Long): Single<UserLocation>

    @Update
    fun updateDefaultLocation(defaultLocation: DefaultLocation)

    @Update
    fun updateUserLocation(userLocation: UserLocation)
}