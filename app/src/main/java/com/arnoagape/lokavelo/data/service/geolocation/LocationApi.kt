package com.arnoagape.lokavelo.data.service.geolocation

import android.location.Location

interface LocationApi {
    suspend fun getLastLocation(): Location?
}