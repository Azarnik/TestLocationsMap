package com.test.epm.locationmap.ui.maps

import com.google.android.gms.maps.model.LatLng
import com.test.epm.locationmap.data.LocationsRepository
import com.test.epm.locationmap.data.local.entity.DefaultLocation
import com.test.epm.locationmap.data.local.entity.UserLocation
import com.test.epm.locationmap.utils.schedulers.BaseSchedulerProvider
import com.test.epm.locationmap.utils.schedulers.ImmediateSchedulerProvider
import dagger.Lazy
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class MapPresenterTest {

    @Mock
    private lateinit var locationsRepository: LocationsRepository
    @Mock
    private lateinit var mapView: MapContract.View

    private lateinit var mapPresenter: MapPresenter

    private val schedulerProvider: BaseSchedulerProvider = ImmediateSchedulerProvider()
    private val throwable: Throwable = Throwable("Excepted error")
    private val time = 1525372836419

    private val defaultLocations = mutableListOf(
            DefaultLocation(1, "d1", 10f, 10f, "", time),
            DefaultLocation(2, "d2", 11f, 10f, "", time),
            DefaultLocation(3, "d3", 13f, 10f, "", time))

    private val userLocations = mutableListOf(
            UserLocation(1, "d1", 10f, 10f, ""),
            UserLocation(2, "d2", 11f, 10f, ""),
            UserLocation(3, "d3", 13f, 10f, ""))

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        `when`(locationsRepository.getDefaultLocations()).thenReturn(Flowable.just(defaultLocations))
        `when`(locationsRepository.getUserLocations()).thenReturn(Flowable.just(userLocations))
        `when`(locationsRepository.checkRemoteUpdates()).thenReturn(Completable.complete())

        mapPresenter = MapPresenter(locationsRepository, schedulerProvider, Lazy { true })

        mapPresenter.attachView(mapView)
    }

    @Test
    fun testLoadDefaultLocations() {
        mapPresenter.loadDefaultLocations()

        verify(mapView).showDefaultLocationsMarkers(defaultLocations)
    }

    @Test
    fun testLoadDefaultLocationError() {
        `when`(locationsRepository.getDefaultLocations()).thenReturn(Flowable.error(throwable))

        mapPresenter.loadDefaultLocations()

        verify(mapView).showLoadingDefaultLocationsError()
    }

    @Test
    fun testLoadUserLocations() {
        mapPresenter.loadUserLocations()

        verify(mapView).showUserLocationsMarkers(userLocations)
    }

    @Test
    fun testLoadUserLocationsError() {
        `when`(locationsRepository.getUserLocations()).thenReturn(Flowable.error(throwable))

        mapPresenter.loadUserLocations()

        verify(mapView).showLoadingUserLocationsError()
    }

    @Test
    fun testUpdateDefaultLocations() {
        mapPresenter.updateDefaultLocations()

        verify(mapView).showDefaultLocationsUpdated()
    }

    @Test
    fun testUpdateDefaultLocationError() {
        `when`(locationsRepository.checkRemoteUpdates()).thenReturn(Completable.error(throwable))

        mapPresenter.updateDefaultLocations()

        verify(mapView).showUpdateDefaultLocationError()
    }

    @Test
    fun testAddUserLocation() {
        `when`(locationsRepository.addUserLocation("TestLocation", 10f, 10f)).thenReturn(Completable.complete())
        val latLng = LatLng(10.0, 10.0)

        mapPresenter.openCreateLocationDialog(latLng)

        verify(mapView).showAddLocationDialog(latLng)

        mapPresenter.addUserLocation("TestLocation", 10f, 10f)

        verify(mapView).showNewLocationAdded()
    }

    @Test
    fun testAddUserLocationError() {
        `when`(locationsRepository.addUserLocation("", 10f, 10f)).thenReturn(Completable.error(throwable))

        mapPresenter.addUserLocation("", 10f, 10f)

        verify(mapView).showAddUserLocationError()
    }

    @Test
    fun testOpenLocationDetails() {
        mapPresenter.openLocationDetails(userLocations[0])

        verify(mapView).showLocationDetailsUi(userLocations[0].id, false)
    }

    @Test
    fun testOpenLocationList() {
        mapPresenter.openLocationList()

        verify(mapView).showLocationListUi()
    }

    @Test
    fun testFetchAllData() {
        mapPresenter.fetchAllData()

        verify(mapView).showDefaultLocationsMarkers(defaultLocations)
        verify(mapView).showUserLocationsMarkers(userLocations)
        verify(mapView).showDefaultLocationsUpdated()
    }
}
