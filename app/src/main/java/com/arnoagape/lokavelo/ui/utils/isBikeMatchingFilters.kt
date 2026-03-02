package com.arnoagape.lokavelo.ui.utils

import com.arnoagape.lokavelo.domain.model.Bike
import com.arnoagape.lokavelo.ui.screen.main.home.SearchFilters

fun isBikeMatchingFilters(
    bike: Bike,
    filters: SearchFilters
): Boolean {

    // 🔎 1. Filtre texte adresse
    filters.addressQuery?.let { query ->
        val normalizedQuery = query.lowercase()

        val matchesAddress =
            bike.location.street.lowercase().contains(normalizedQuery) ||
                    bike.location.city.lowercase().contains(normalizedQuery) ||
                    bike.title.lowercase().contains(normalizedQuery)

        if (!matchesAddress) return false
    }

    // 📍 2. Filtre distance
    if (filters.center != null && filters.maxDistanceKm != null) {

        val results = FloatArray(1)

        android.location.Location.distanceBetween(
            filters.center.latitude,
            filters.center.longitude,
            bike.location.latitude,
            bike.location.longitude,
            results
        )

        val distanceKm = results[0] / 1000.0

        if (distanceKm > filters.maxDistanceKm) return false
    }

    return true
}