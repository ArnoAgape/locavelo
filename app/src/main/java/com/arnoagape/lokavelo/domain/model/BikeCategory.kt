package com.arnoagape.lokavelo.domain.model

import androidx.annotation.StringRes
import com.arnoagape.lokavelo.R

enum class BikeCategory {
    ROAD, MTB, CITY, GRAVEL, BMX, TREKKING, TANDEM, CARGO, LONGTAIL, FOLDING, KID
}

@StringRes
fun BikeCategory.labelRes(): Int =
    when (this) {
        BikeCategory.ROAD -> R.string.bike_category_road
        BikeCategory.MTB -> R.string.bike_category_mtb
        BikeCategory.CITY -> R.string.bike_category_city
        BikeCategory.GRAVEL -> R.string.bike_category_gravel
        BikeCategory.BMX -> R.string.bike_category_bmx
        BikeCategory.TREKKING -> R.string.bike_category_trekking
        BikeCategory.TANDEM -> R.string.bike_category_tandem
        BikeCategory.CARGO -> R.string.bike_category_cargo
        BikeCategory.LONGTAIL -> R.string.bike_category_longtail
        BikeCategory.FOLDING -> R.string.bike_category_folding
        BikeCategory.KID -> R.string.bike_category_kid
    }