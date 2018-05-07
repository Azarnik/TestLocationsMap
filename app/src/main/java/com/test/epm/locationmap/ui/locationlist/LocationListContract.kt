package com.test.epm.locationmap.ui.locationlist

import com.test.epm.locationmap.data.local.entity.BaseLocation
import com.test.epm.locationmap.ui.BasePresenter
import com.test.epm.locationmap.ui.BaseView
import com.test.epm.locationmap.ui.locationlist.entity.LocationDistance

interface LocationListContract {
    interface View : BaseView {
        fun showLocationsList(locationsList: List<LocationDistance>)
        fun showLocationDetailUI(id: Long, isDefault: Boolean)
        fun showGetLocationsError()
    }

    interface Presenter : BasePresenter<View> {
        fun openLocationDetail(location: BaseLocation)
        fun getAllLocations()
    }
}