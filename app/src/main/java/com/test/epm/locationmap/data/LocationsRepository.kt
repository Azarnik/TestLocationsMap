package com.test.epm.locationmap.data

import com.test.epm.locationmap.data.local.LocalDataSource
import com.test.epm.locationmap.data.local.entity.BaseLocation
import com.test.epm.locationmap.data.local.entity.DefaultLocation
import com.test.epm.locationmap.data.local.entity.UserLocation
import com.test.epm.locationmap.data.remote.RemoteDateSource
import com.test.epm.locationmap.data.remote.entity.LocationsList
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationsRepository @Inject
internal constructor(
        private val remoteDateSource: RemoteDateSource,
        private val localDataSource: LocalDataSource) {

    fun getDefaultLocations(): Flowable<List<DefaultLocation>> = localDataSource.defaultLocations()

    fun getUserLocations(): Flowable<List<UserLocation>> = localDataSource.userLocations()

    fun getAllLocations(): Flowable<List<BaseLocation>> = Flowable.zip(
            localDataSource.defaultLocations(), localDataSource.userLocations(),
            BiFunction<List<DefaultLocation>, List<UserLocation>, List<BaseLocation>> { t1, t2 ->
                toBaseLocations(t1, t2)
            })

    fun checkRemoteUpdates(): Completable = remoteDateSource.getLocations()
            .flatMapCompletable { it: LocationsList -> saveDefaultLocations(it) }


    fun addUserLocation(name: String, lat: Float, lng: Float): Completable =
            Completable.fromAction({
                if (name.isBlank()) {
                    throw Throwable("Location name cannot be empty")
                } else {
                    localDataSource.insertUserLocation(UserLocation(0, name, lat, lng, null))
                }
            })

    fun getLocation(id: Long, isDefault: Boolean): Single<BaseLocation> =
            if (isDefault)
                localDataSource.getDefaultLocationById(id).map { t -> t as BaseLocation }
            else
                localDataSource.getUserLocationById(id).map { t -> t as BaseLocation }


    fun updateLocationDescription(location: BaseLocation?, note: String): Completable = Completable.fromCallable({
        if (location is DefaultLocation) {
            localDataSource.updateDefaultLocation(location.copy(description = note))
        } else if (location is UserLocation) {
            localDataSource.updateUserLocation(location.copy(description = note))
        }
    })

    private fun saveDefaultLocations(locationsList: LocationsList): Completable = Completable.fromCallable({
        val lastUpdatedTime = localDataSource.getDefaultLocationUpdatedTime()
        if (lastUpdatedTime < locationsList.updated.time) {
            val defaultLocations: MutableList<DefaultLocation> = mutableListOf()
            for (location in locationsList.locations)
                defaultLocations.add(location.copy(updatedAt = locationsList.updated.time))
            localDataSource.replaceDefaultLocations(defaultLocations)
        }
    })


    private fun toBaseLocations(def: List<DefaultLocation>, user: List<UserLocation>): List<BaseLocation> {
        val list: MutableList<BaseLocation> = mutableListOf()
        list.addAll(def)
        list.addAll(user)
        return list
    }
}
