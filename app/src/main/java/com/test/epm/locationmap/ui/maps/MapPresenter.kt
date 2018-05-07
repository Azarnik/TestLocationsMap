package com.test.epm.locationmap.ui.maps

import com.google.android.gms.maps.model.LatLng
import com.test.epm.locationmap.data.LocationsRepository
import com.test.epm.locationmap.data.local.entity.BaseLocation
import com.test.epm.locationmap.data.local.entity.DefaultLocation
import com.test.epm.locationmap.di.ActivityScoped
import com.test.epm.locationmap.utils.schedulers.BaseSchedulerProvider
import dagger.Lazy
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@ActivityScoped
class MapPresenter @Inject
constructor(
        private val locationsRepository: LocationsRepository,
        private val schedulerProvider: BaseSchedulerProvider,
        private val isFirstStart: Lazy<Boolean>
) : MapContract.Presenter {

    private var mView: MapContract.View? = null
    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun attachView(view: MapContract.View) {
        this.mView = view
    }

    override fun detachView() {
        this.mView = null
        disposable.clear()
    }

    override fun loadDefaultLocations() {
        disposable.add(locationsRepository.getDefaultLocations()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    mView?.showDefaultLocationsMarkers(it)
                }, { mView?.showLoadingDefaultLocationsError() })
        )
    }

    override fun loadUserLocations() {
        disposable.add(locationsRepository.getUserLocations()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    mView?.showUserLocationsMarkers(it)
                }, { mView?.showLoadingUserLocationsError() })
        )
    }

    override fun updateDefaultLocations() {
        disposable.add(locationsRepository.checkRemoteUpdates()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ mView?.showDefaultLocationsUpdated() }, { mView?.showUpdateDefaultLocationError() }))
    }

    override fun addUserLocation(name: String, lat: Float, lng: Float) {
        disposable.add(locationsRepository.addUserLocation(name, lat, lng)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({mView?.showNewLocationAdded()}, { mView?.showAddUserLocationError() }))
    }

    override fun openLocationDetails(location: BaseLocation) {
        val isDefault: Boolean = location is DefaultLocation
        mView?.showLocationDetailsUi(location.id, isDefault)
    }

    override fun openCreateLocationDialog(latLng: LatLng) {
        mView?.showAddLocationDialog(latLng)
    }

    override fun openLocationList() {
        mView?.showLocationListUi()
    }

    override fun fetchAllData() {
        loadDefaultLocations()
        loadUserLocations()
        if (isFirstStart.get())
            updateDefaultLocations()
    }
}