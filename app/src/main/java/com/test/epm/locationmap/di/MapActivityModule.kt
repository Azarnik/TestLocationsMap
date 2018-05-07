package com.test.epm.locationmap.di

import com.test.epm.locationmap.ui.maps.MapActivity
import com.test.epm.locationmap.ui.maps.MapContract
import com.test.epm.locationmap.ui.maps.MapPresenter
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class MapActivityModule {
    @ActivityScoped
    @Binds
    abstract fun providesMapsPresenter(presenter: MapPresenter): MapContract.Presenter

    @Module
    companion object {
        @JvmStatic
        @Provides
        @ActivityScoped
        fun providesIsFirstStart(activity: MapActivity): Boolean = activity.isFirstStart()
    }
}