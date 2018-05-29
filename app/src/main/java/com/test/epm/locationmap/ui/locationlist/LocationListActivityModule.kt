package com.test.epm.locationmap.ui.locationlist

import com.test.epm.locationmap.di.ActivityScoped
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
abstract class LocationListActivityModule {
    @ActivityScoped
    @Binds
    abstract fun providesLocationListPresenter(presenter: LocationListPresenter): LocationListContract.Presenter

    @Module
    companion object {
        @JvmStatic
        @Provides
        @ActivityScoped
        @Named(LocationListActivity.ARGS_LATITUDE)
        fun providesDeviceLatitude(activity: LocationListActivity): Double =
                activity.intent.getDoubleExtra(LocationListActivity.ARGS_LATITUDE, 0.0)

        @JvmStatic
        @Provides
        @ActivityScoped
        @Named(LocationListActivity.ARGS_LONGITUDE)
        fun providesDeviceLongitude(activity: LocationListActivity): Double =
                activity.intent.getDoubleExtra(LocationListActivity.ARGS_LONGITUDE, 0.0)
    }
}