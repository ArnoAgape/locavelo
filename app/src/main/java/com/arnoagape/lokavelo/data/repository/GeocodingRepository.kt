package com.arnoagape.lokavelo.data.repository

import com.arnoagape.lokavelo.domain.model.AddressSuggestion

interface GeocodingRepository {
    suspend fun search(query: String): List<AddressSuggestion>
}