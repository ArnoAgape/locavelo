package com.arnoagape.lokavelo.ui.screen.owner.rental

import com.arnoagape.lokavelo.ui.screen.owner.homeBike.RentalWithBike

sealed interface HomeRentalUiState {
    data object Loading : HomeRentalUiState
    data class Success(val rentals: List<RentalWithBike>) : HomeRentalUiState
    data object Empty : HomeRentalUiState

    sealed interface Error : HomeRentalUiState {
        data object Generic : Error
    }
}