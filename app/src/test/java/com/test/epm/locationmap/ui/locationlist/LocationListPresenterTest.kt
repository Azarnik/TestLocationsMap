package com.test.epm.locationmap.ui.locationlist

import com.test.epm.locationmap.data.LocationsRepository
import com.test.epm.locationmap.data.local.entity.BaseLocation
import com.test.epm.locationmap.data.local.entity.DefaultLocation
import com.test.epm.locationmap.utils.schedulers.BaseSchedulerProvider
import com.test.epm.locationmap.utils.schedulers.ImmediateSchedulerProvider
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class LocationListPresenterTest {

    @Mock
    private lateinit var locationsRepository: LocationsRepository
    @Mock
    private lateinit var detailView: LocationListContract.View

    private lateinit var presenter: LocationListPresenter

    private val schedulerProvider: BaseSchedulerProvider = ImmediateSchedulerProvider()
    private val deviceLatitude = 10.0
    private val deviceLongitude = 10.0
    private val defaultLocations = mutableListOf(
            DefaultLocation(1, "d1", 10f, 10f, "", 0),
            DefaultLocation(1, "d2", 13f, 10f, "", 0),
            DefaultLocation(2, "d3", 11f, 10f, "", 0))

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        presenter = LocationListPresenter(locationsRepository, schedulerProvider, deviceLatitude, deviceLongitude)
        presenter.attachView(detailView)

        `when`(locationsRepository.getAllLocations()).thenReturn(Flowable.just(defaultLocations))
    }

    @Test
    fun testGetAllLocations() {
        presenter.getAllLocations()

        verify(detailView).showLocationsList(Mockito.anyList())
    }

    @Test
    fun testGetAllLocationsError() {
        `when`(locationsRepository.getAllLocations()).thenReturn(Flowable.error(Throwable("Excepted error")))

        presenter.getAllLocations()

        verify(detailView).showGetLocationsError()
    }

    @Test
    fun testOpenLocationDetail() {
        val baseLocation: BaseLocation = defaultLocations[0]
        val isDefault = defaultLocations[0] is DefaultLocation
        presenter.openLocationDetail(baseLocation)

        verify(detailView).showLocationDetailUI(baseLocation.id, isDefault)
    }
}