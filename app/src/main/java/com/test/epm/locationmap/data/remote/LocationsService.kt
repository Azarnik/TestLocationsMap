package com.test.epm.locationmap.data.remote

import com.test.epm.locationmap.data.remote.entity.LocationsList
import io.reactivex.Observable
import retrofit2.http.GET

interface LocationsService {
    @GET("locations.json")
    fun getLocations(): Observable<LocationsList>
}