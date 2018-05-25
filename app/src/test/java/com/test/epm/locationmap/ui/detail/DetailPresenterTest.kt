package com.test.epm.locationmap.ui.detail

import com.test.epm.locationmap.data.LocationsRepository
import com.test.epm.locationmap.data.local.entity.UserLocation
import com.test.epm.locationmap.utils.schedulers.BaseSchedulerProvider
import com.test.epm.locationmap.utils.schedulers.ImmediateSchedulerProvider
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class DetailPresenterTest {

    @Mock
    private lateinit var locationsRepository: LocationsRepository
    @Mock
    private lateinit var detailView: DetailContract.View

    private lateinit var detailPresenter: DetailPresenter

    private val schedulerProvider: BaseSchedulerProvider = ImmediateSchedulerProvider()
    private val locationId: Long = 1
    private val isDefault = false
    private val userLocation = UserLocation(locationId, "test", 10f, 10f, null)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        detailPresenter = DetailPresenter(locationsRepository, schedulerProvider, locationId, isDefault)
        detailPresenter.attachView(detailView)
    }

    @Test
    fun testGetLocationDetails() {
        `when`(locationsRepository.getLocation(locationId, isDefault)).thenReturn(Single.just(userLocation))

        detailPresenter.getLocationDetails()

        verify(detailView).showLocationDetail(userLocation)
    }

    @Test
    fun testGetLocationDetailsError() {
        `when`(locationsRepository.getLocation(locationId, isDefault)).thenReturn(Single.error(Throwable("Excepted error")))

        detailPresenter.getLocationDetails()

        verify(detailView).showGetLocationDetailsError()
    }
}