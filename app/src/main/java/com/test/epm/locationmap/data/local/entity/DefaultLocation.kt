package com.test.epm.locationmap.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "default_locations")
data class DefaultLocation(@PrimaryKey(autoGenerate = true) override val id: Long,
                           override val name: String,
                           override val lat: Float,
                           override val lng: Float,
                           override val description: String?,
                           var updatedAt: Long?) : BaseLocation()