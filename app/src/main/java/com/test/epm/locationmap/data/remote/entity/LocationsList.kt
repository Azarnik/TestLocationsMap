package com.test.epm.locationmap.data.remote.entity

import com.test.epm.locationmap.data.local.entity.DefaultLocation
import java.util.*

data class LocationsList(val locations: List<DefaultLocation>, val updated: Date)