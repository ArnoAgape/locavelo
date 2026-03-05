package com.arnoagape.lokavelo.domain.model

import androidx.annotation.StringRes
import com.arnoagape.lokavelo.R

enum class BikeAvailability {
    AVAILABLE, UNAVAILABLE, RENTING
}

@StringRes
fun BikeAvailability.labelRes(): Int =
    when (this) {
        BikeAvailability.AVAILABLE -> R.string.bike_availability_yes
        BikeAvailability.RENTING -> R.string.bike_availability_renting
        BikeAvailability.UNAVAILABLE -> R.string.bike_availability_no
    }