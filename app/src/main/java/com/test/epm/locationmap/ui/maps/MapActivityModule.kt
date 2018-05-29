package com.test.epm.locationmap.ui.maps

import com.test.epm.locationmap.di.ActivityScoped
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