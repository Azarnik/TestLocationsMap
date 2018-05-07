package com.test.epm.locationmap.data.remote

import com.test.epm.locationmap.data.remote.entity.LocationsList
import io.reactivex.Observable

interface RemoteDateSource {
    fun getLocations(): Observable<LocationsList>
}

class RemoteDateSourceImpl
constructor(private val locationsService: LocationsService) : RemoteDateSource {
    override fun getLocations(): Observable<LocationsList> {
        return locationsService.getLocations()
    }
}