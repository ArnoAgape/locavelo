package com.arnoagape.lokavelo.domain.model

import androidx.annotation.StringRes
import com.arnoagape.lokavelo.R

enum class RentalStatus {
    PENDING,
    ACCEPTED,
    DECLINED,
    CANCELLED,
    ACTIVE,
    COMPLETED
}

@StringRes
fun RentalStatus.labelRes(): Int =
    when (this) {
        RentalStatus.PENDING -> R.string.bike_availability_yes
        RentalStatus.ACCEPTED -> R.string.bike_availability_renting
        RentalStatus.DECLINED -> R.string.bike_availability_no
        RentalStatus.CANCELLED -> R.string.bike_availability_yes
        RentalStatus.ACTIVE -> R.string.bike_availability_renting
        RentalStatus.COMPLETED -> R.string.bike_availability_yes
    }