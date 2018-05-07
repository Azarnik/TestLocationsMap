package com.test.epm.locationmap.ui.maps

import com.google.android.gms.maps.model.LatLng
import com.test.epm.locationmap.data.local.entity.BaseLocation
import com.test.epm.locationmap.data.local.entity.DefaultLocation
import com.test.epm.locationmap.data.local.entity.UserLocation
import com.test.epm.locationmap.ui.BasePresenter
import com.test.epm.locationmap.ui.BaseView

interface MapContract {
    interface View : BaseView {
        fun showDefaultLocationsMarkers(locationsList: List<DefaultLocation>)
        fun showUserLocationsMarkers(locationsList: List<UserLocation>)
        fun clearDefaultMarkers()
        fun clearUserMarkers()
        fun showLocationListUi()
        fun showLocationDetailsUi(id: Long, isDefault: Boolean)
        fun showUpdateDefaultLocationError()
        fun showAddLocationDialog(latLng: LatLng)
        fun showDefaultLocationsUpdated()
        fun showLoadingDefaultLocationsError()
        fun showLoadingUserLocationsError()
        fun showAddUserLocationError()
        fun showNewLocationAdded()
    }

    interface Presenter : BasePresenter<View> {
        fun addUserLocation(name: String, lat: Float, lng: Float)
        fun openLocationDetails(location: BaseLocation)
        fun openCreateLocationDialog(latLng: LatLng)
        fun loadDefaultLocations()
        fun loadUserLocations()
        fun updateDefaultLocations()
        fun fetchAllData()
        fun openLocationList()
    }
}