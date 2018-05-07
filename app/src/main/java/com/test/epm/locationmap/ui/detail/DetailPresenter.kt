package com.test.epm.locationmap.ui.detail

import com.test.epm.locationmap.data.LocationsRepository
import com.test.epm.locationmap.data.local.entity.BaseLocation
import com.test.epm.locationmap.di.ActivityScoped
import com.test.epm.locationmap.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@ActivityScoped
class DetailPresenter @Inject
constructor(
        private val locationsRepository: LocationsRepository,
        private val schedulerProvider: BaseSchedulerProvider,
        private val locationId: Long,
        private val isDefault: Boolean
) : DetailContract.Presenter {
    private val disposable: CompositeDisposable = CompositeDisposable()
    private var view: DetailContract.View? = null
    private var location: BaseLocation? = null

    override fun updateDescription(note: String) {
        disposable.add(locationsRepository.updateLocationDescription(location, note)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe())
    }

    override fun attachView(view: DetailContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
        disposable.clear()
    }

    override fun getLocationDetails() {
        disposable.add(locationsRepository.getLocation(locationId, isDefault)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    view?.showLocationDetail(it)
                    location = it
                }, { view?.showGetLocationError() }))
    }
}