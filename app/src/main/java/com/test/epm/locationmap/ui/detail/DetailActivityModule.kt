package com.test.epm.locationmap.ui.detail

import com.test.epm.locationmap.di.ActivityScoped
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class DetailActivityModule {
    @ActivityScoped
    @Binds
    abstract fun providesDetailPresenter(presenter: DetailPresenter): DetailContract.Presenter

    @Module
    companion object {
        @JvmStatic
        @Provides
        @ActivityScoped
        fun providesLocationId(activity: DetailActivity): Long =
                activity.intent.getLongExtra(DetailActivity.ARGS_LOCATION_ID, 0)

        @JvmStatic
        @Provides
        @ActivityScoped
        fun providesIsDefaultLocation(activity: DetailActivity): Boolean =
                activity.intent.getBooleanExtra(DetailActivity.ARGS_IS_DEFAULT_LOCATION, false)
    }
}
