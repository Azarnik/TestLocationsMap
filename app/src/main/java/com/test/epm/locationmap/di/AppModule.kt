package com.test.epm.locationmap.di

import android.app.Application
import android.content.Context
import com.test.epm.locationmap.utils.schedulers.BaseSchedulerProvider
import com.test.epm.locationmap.utils.schedulers.SchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
abstract class AppModule {
    @Binds
    abstract fun bindContext(application: Application): Context

    @Module
    companion object {
        @JvmStatic
        @Provides
        @Singleton
        fun providesScheduler(): BaseSchedulerProvider = SchedulerProvider()
    }
}
