package com.arnoagape.lokavelo.data.network.nominatim

import com.arnoagape.lokavelo.data.repository.GeocodingRepository
import com.arnoagape.lokavelo.domain.model.AddressSuggestion

class NominatimRepositoryImpl(
    private val api: NominatimApi
) : GeocodingRepository {

    override suspend fun search(query: String): List<AddressSuggestion> {
        return api.search(query)
            .map { it.toDomain() }
    }
}