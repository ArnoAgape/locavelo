package com.arnoagape.lokavelo.ui.screen.main.detail

import com.arnoagape.lokavelo.domain.model.Bike

sealed class DetailPublicBikeUiState {

    object Idle : DetailPublicBikeUiState()
    object Loading : DetailPublicBikeUiState()
    data class Success(val bike: Bike) : DetailPublicBikeUiState()
    sealed class Error : DetailPublicBikeUiState() {
        data class Generic(val message: String = "Unknown error") : Error()
        data class Network(val isNetworkError: Boolean) : Error()
    }
}