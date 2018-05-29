package com.test.epm.locationmap.data.remote

import com.test.epm.locationmap.data.remote.entity.LocationsList
import io.reactivex.Observable

class RemoteDateSourceImpl
constructor(private val locationsService: LocationsService) : RemoteDataSource {
    override fun getLocations(): Observable<LocationsList> {
        return locationsService.getLocations()
    }
}