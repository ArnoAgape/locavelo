package com.arnoagape.lokavelo.ui.screen.owner.detail

import com.arnoagape.lokavelo.domain.model.Bike

/**
 * Represents the UI state for the detail screen.
 */
sealed class DetailBikeUiState {

    object Loading : DetailBikeUiState()
    data class Success(val bike: Bike) : DetailBikeUiState()
    object Deleting : DetailBikeUiState()
    sealed class Error : DetailBikeUiState() {
        data class Generic(val message: String = "Unknown error") : Error()
    }
}