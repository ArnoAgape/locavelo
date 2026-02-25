package com.arnoagape.lokavelo.data.network.nominatim

import com.arnoagape.lokavelo.domain.model.AddressSuggestion
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NominatimDto(
    @param:Json(name = "display_name")
    val displayName: String,

    val lat: String,
    val lon: String,

    val address: AddressDto?
)

@JsonClass(generateAdapter = true)
data class AddressDto(
    val road: String?,
    val city: String?,
    val town: String?,
    val village: String?,
    val postcode: String?,
    val country: String?
)

fun NominatimDto.toDomain(): AddressSuggestion =
    AddressSuggestion(
        displayName = displayName,
        lat = lat.toDoubleOrNull() ?: 0.0,
        lon = lon.toDoubleOrNull() ?: 0.0,
        street = address?.road,
        city = address?.city ?: address?.town ?: address?.village,
        postalCode = address?.postcode,
        country = address?.country
    )