package com.test.epm.locationmap.data.remote

import com.test.epm.locationmap.data.remote.entity.LocationsList
import io.reactivex.Observable

interface RemoteDataSource {
    fun getLocations(): Observable<LocationsList>
}
