package com.test.epm.locationmap.data.local.entity

abstract class BaseLocation {
    abstract val id: Long
    abstract val name: String
    abstract val lat: Float
    abstract val lng: Float
    abstract val description: String?
}
