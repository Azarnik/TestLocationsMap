package com.test.epm.locationmap.di

import com.test.epm.locationmap.ui.detail.DetailActivity
import com.test.epm.locationmap.ui.detail.DetailContract
import com.test.epm.locationmap.ui.detail.DetailPresenter
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
