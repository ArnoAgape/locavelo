package com.arnoagape.lokavelo.data.repository

import com.arnoagape.lokavelo.data.service.rental.RentalApi
import com.arnoagape.lokavelo.domain.model.Rental
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that manages bike-related operations.
 * Delegates data access to [RentalApi] to provide
 * a clean abstraction layer for ViewModels.
 */
@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class RentalRepository @Inject constructor(
    private val rentalApi: RentalApi,
    private val userRepository: UserRepository
) {

    fun observeOwnerRentals(): Flow<List<Rental>> = rentalApi.observeOwnerRentals()
}