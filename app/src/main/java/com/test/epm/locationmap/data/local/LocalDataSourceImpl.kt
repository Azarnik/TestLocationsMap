package com.test.epm.locationmap.data.local

import com.test.epm.locationmap.data.local.entity.DefaultLocation
import com.test.epm.locationmap.data.local.entity.UserLocation
import io.reactivex.Flowable
import io.reactivex.Single

class LocalDataSourceImpl constructor(private val locationsDao: LocationsDao) : LocalDataSource {
    override fun userLocations(): Flowable<List<UserLocation>> = locationsDao.userLocations()

    override fun insertUserLocation(userLocation: UserLocation) = locationsDao.insertLocation(userLocation)

    override fun defaultLocations(): Flowable<List<DefaultLocation>> = locationsDao.defaultLocations()

    override fun replaceDefaultLocations(defaultLocations: List<DefaultLocation>) {
        locationsDao.clearDefaultLocationsTable()
        locationsDao.insertAllDefaultLocations(defaultLocations)
    }

    override fun getDefaultLocationUpdatedTime(): Long = locationsDao.getDefaultUpdatedAt()

    override fun getUserLocationById(id: Long): Single<UserLocation> = locationsDao.getUserLocationById(id)

    override fun getDefaultLocationById(id: Long): Single<DefaultLocation> = locationsDao.getDefaultLocationById(id)

    override fun updateDefaultLocation(defaultLocation: DefaultLocation) {
        locationsDao.updateDefaultLocation(defaultLocation)
    }

    override fun updateUserLocation(userLocation: UserLocation) {
        locationsDao.updateUserLocation(userLocation)
    }

    override fun deleteUserLocation(userLocation: UserLocation) {
        return locationsDao.deleteUserLocation(userLocation)
    }
}