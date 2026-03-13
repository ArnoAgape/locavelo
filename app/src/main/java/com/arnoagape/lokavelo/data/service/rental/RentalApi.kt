package com.arnoagape.lokavelo.data.service.rental

import com.arnoagape.lokavelo.domain.model.Bike
import com.arnoagape.lokavelo.domain.model.Rental
import kotlinx.coroutines.flow.Flow

interface RentalApi {

    fun observeOwnerRentals(): Flow<List<Rental>>

}