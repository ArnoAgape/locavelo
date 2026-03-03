package com.arnoagape.lokavelo.ui.screen.owner.addBike

import com.arnoagape.lokavelo.ui.screen.owner.editBike.EditBikeUiState.Error

sealed class AddBikeUiState {

    object Idle : AddBikeUiState()
    object Loading : AddBikeUiState()
    sealed class Error : AddBikeUiState() {
        data class NoAccount(val message: String = "No account found") : Error()
        data class Generic(val message: String = "Unknown error") : Error()
        data class Network(val isNetworkError: Boolean) : Error()
        data class Initial(val isNetworkError: Boolean) : Error()
    }
}