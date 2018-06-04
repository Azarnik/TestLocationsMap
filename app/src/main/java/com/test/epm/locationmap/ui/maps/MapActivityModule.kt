package com.test.epm.locationmap.ui.maps

import com.test.epm.locationmap.di.ActivityScoped
import dagger.Module
import dagger.Provides

@Module
class MapActivityModule {
    @Provides
    @ActivityScoped
    fun providesIsFirstStart(activity: MapActivity): Boolean = activity.isFirstStart()
}