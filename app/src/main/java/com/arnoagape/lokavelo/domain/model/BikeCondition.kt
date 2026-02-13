package com.arnoagape.lokavelo.domain.model

import androidx.annotation.StringRes
import com.arnoagape.lokavelo.R

enum class BikeCondition {
    LIKE_NEW, EXCELLENT, GOOD, USED
}

@StringRes
fun BikeCondition.labelRes(): Int =
    when (this) {
        BikeCondition.LIKE_NEW -> R.string.bike_state_like_new
        BikeCondition.EXCELLENT -> R.string.bike_state_excellent
        BikeCondition.GOOD -> R.string.bike_state_good
        BikeCondition.USED -> R.string.bike_state_used
    }
