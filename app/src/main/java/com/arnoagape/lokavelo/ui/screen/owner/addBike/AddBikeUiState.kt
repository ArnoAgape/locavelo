package com.arnoagape.lokavelo.ui.screen.owner.addBike

import com.arnoagape.lokavelo.ui.screen.owner.editBike.EditBikeUiState

sealed class AddBikeUiState {

    object Idle : AddBikeUiState()
    object Loading : AddBikeUiState()
    object Success : AddBikeUiState()
    object Submitting : AddBikeUiState()
    sealed class Error : AddBikeUiState() {
        data class NoAccount(val message: String = "No account found") : Error()
        data class Generic(val message: String = "Unknown error") : Error()
    }
}