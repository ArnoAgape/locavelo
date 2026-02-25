package com.arnoagape.lokavelo.data.network.nominatim

import retrofit2.http.GET
import retrofit2.http.Query

interface NominatimApi {

    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("addressdetails") addressDetails: Int = 1,
        @Query("limit") limit: Int = 5,
        @Query("countrycodes") countryCodes: String = "fr"
    ): List<NominatimDto>
}