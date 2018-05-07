package com.test.epm.locationmap.di

import android.app.Application
import android.arch.persistence.room.Room
import com.google.gson.GsonBuilder
import com.test.epm.locationmap.data.local.LocalDataSource
import com.test.epm.locationmap.data.local.LocalDataSourceImpl
import com.test.epm.locationmap.data.local.LocationsDao
import com.test.epm.locationmap.data.local.LocationsDatabase
import com.test.epm.locationmap.data.remote.LocationsService
import com.test.epm.locationmap.data.remote.RemoteDateSource
import com.test.epm.locationmap.data.remote.RemoteDateSourceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun providesLocationDao(database: LocationsDatabase): LocationsDao = database.locationsDao()

    @Provides
    @Singleton
    fun providesDb(application: Application): LocationsDatabase =
            Room.databaseBuilder(
                    application.applicationContext,
                    LocationsDatabase::class.java,
                    "Locations.db").build()

    @Provides
    @Singleton
    fun providesLocalRepos(dao: LocationsDao): LocalDataSource = LocalDataSourceImpl(dao)

    @Provides
    @Singleton
    fun providesRemoteRepos(retrofit: Retrofit): RemoteDateSource = RemoteDateSourceImpl(retrofit.create(LocationsService::class.java))

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                    .create()))
            .baseUrl("https://s3-ap-southeast-2.amazonaws.com/com-cochlear-sabretooth-takehometest/")
            .build()
}
