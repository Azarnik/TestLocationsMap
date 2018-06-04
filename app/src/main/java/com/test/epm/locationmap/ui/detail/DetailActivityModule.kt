package com.test.epm.locationmap.ui.detail

import com.test.epm.locationmap.di.ActivityScoped
import dagger.Module
import dagger.Provides

@Module
class DetailActivityModule {
    @Provides
    @ActivityScoped
    fun providesLocationId(activity: DetailActivity): Long =
            activity.intent.getLongExtra(DetailActivity.ARGS_LOCATION_ID, 0)

    @Provides
    @ActivityScoped
    fun providesIsDefaultLocation(activity: DetailActivity): Boolean =
            activity.intent.getBooleanExtra(DetailActivity.ARGS_IS_DEFAULT_LOCATION, false)

}
