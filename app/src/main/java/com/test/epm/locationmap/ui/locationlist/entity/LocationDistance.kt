package com.test.epm.locationmap.ui.locationlist.entity

import android.location.Location
import com.test.epm.locationmap.data.local.entity.BaseLocation

class LocationDistance constructor(val location: BaseLocation, lat: Double, lng: Double) {
    val distance: Int

    init {
        val result = FloatArray(2)
        Location.distanceBetween(location.lat.toDouble(), location.lng.toDouble(), lat, lng, result)
        distance = result[0].toInt()
    }
}