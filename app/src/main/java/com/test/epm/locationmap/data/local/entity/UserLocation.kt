package com.test.epm.locationmap.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "user_locations")
data class UserLocation(@PrimaryKey(autoGenerate = true) override val id: Long,
                        override val name: String,
                        override val lat: Float,
                        override val lng: Float,
                        override val description: String?) : BaseLocation()