package com.test.epm.locationmap.ui.locationlist

import com.test.epm.locationmap.data.LocationsRepository
import com.test.epm.locationmap.data.local.entity.BaseLocation
import com.test.epm.locationmap.data.local.entity.DefaultLocation
import com.test.epm.locationmap.di.ActivityScoped
import com.test.epm.locationmap.ui.locationlist.entity.LocationDistance
import com.test.epm.locationmap.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Named

@ActivityScoped
class LocationListPresenter @Inject
constructor(
        private val locationsRepository: LocationsRepository,
        private val schedulerProvider: BaseSchedulerProvider,
        @Named(LocationListActivity.ARGS_LATITUDE) private val deviceLatitude: Double,
        @Named(LocationListActivity.ARGS_LONGITUDE) private val deviceLongitude: Double
) : LocationListContract.Presenter {

    private var view: LocationListContract.View? = null
    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun attachView(view: LocationListContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
        disposable.clear()
    }

    override fun openLocationDetail(location: BaseLocation) {
        val isDefault: Boolean = location is DefaultLocation
        view?.showLocationDetailUI(location.id, isDefault)
    }

    override fun getAllLocations() {
        locationsRepository.getAllLocations()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .map { it -> mapToLocationDistanceList(it) }
                .subscribe({
                    view?.showLocationsList(it.sortedBy { it.distance })
                }, { view?.showGetLocationsError() })
    }

    private fun mapToLocationDistanceList(locations: List<BaseLocation>): List<LocationDistance> {
        val list: MutableList<LocationDistance> = mutableListOf()
        for (location in locations) {
            list.add(LocationDistance(location, deviceLatitude, deviceLongitude))
        }
        return list
    }
}