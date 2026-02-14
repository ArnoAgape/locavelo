package com.arnoagape.lokavelo.domain.model

data class BikeLocation(
    val street: String = "",
    val streetNumber: String = "",
    val addressLine2: String = "",
    val postalCode: String = "",
    val city: String = "",
    val country: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
)
