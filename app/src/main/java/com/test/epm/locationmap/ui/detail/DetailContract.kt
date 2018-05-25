package com.test.epm.locationmap.ui.detail

import com.test.epm.locationmap.data.local.entity.BaseLocation
import com.test.epm.locationmap.ui.BasePresenter
import com.test.epm.locationmap.ui.BaseView

interface DetailContract {
    interface View : BaseView {
        fun showLocationDetail(location: BaseLocation)
        fun showGetLocationDetailsError()
        fun closeDetailsScreen()
        fun showCannotDeleteDefaultLocation()
        fun showDeleteConfirmationDialog()
    }

    interface Presenter : BasePresenter<View> {
        fun updateDescription(note: String)
        fun getLocationDetails()
        fun deleteLocation()
    }
}