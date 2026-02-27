package com.arnoagape.lokavelo.domain.model

data class BikeLocation(
    val street: String = "",
    val postalCode: String = "",
    val city: String = "",
    val country: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

data class AddressSuggestion(
    val displayName: String,
    val lat: Double,
    val lon: Double,
    val street: String?,
    val city: String?,
    val postalCode: String?,
    val country: String?
)