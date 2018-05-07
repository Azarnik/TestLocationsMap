package com.test.epm.locationmap.di

import com.test.epm.locationmap.ui.detail.DetailActivity
import com.test.epm.locationmap.ui.locationlist.LocationListActivity
import com.test.epm.locationmap.ui.maps.MapActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = [(MapActivityModule::class)])
    abstract fun mapActivity(): MapActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [(LocationsListActivityModule::class)])
    abstract fun locationListActivity(): LocationListActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [(DetailActivityModule::class)])
    abstract fun detailActivity(): DetailActivity

}
