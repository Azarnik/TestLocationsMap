package com.test.epm.locationmap.data.local

import com.test.epm.locationmap.data.local.entity.DefaultLocation
import com.test.epm.locationmap.data.local.entity.UserLocation
import io.reactivex.Flowable
import io.reactivex.Single

interface LocalDataSource {
    fun userLocations(): Flowable<List<UserLocation>>
    fun insertUserLocation(userLocation: UserLocation)
    fun defaultLocations(): Flowable<List<DefaultLocation>>
    fun replaceDefaultLocations(defaultLocations: List<DefaultLocation>)
    fun getDefaultLocationUpdatedTime(): Long
    fun getUserLocationById(id: Long): Single<UserLocation>
    fun getDefaultLocationById(id: Long): Single<DefaultLocation>
    fun updateDefaultLocation(defaultLocation: DefaultLocation)
    fun updateUserLocation(userLocation: UserLocation)
    fun deleteUserLocation(userLocation: UserLocation)
}