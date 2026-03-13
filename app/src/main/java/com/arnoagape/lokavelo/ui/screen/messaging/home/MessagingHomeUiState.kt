package com.arnoagape.lokavelo.ui.screen.messaging.home

import androidx.annotation.StringRes
import com.arnoagape.lokavelo.R

sealed class MessagingHomeUiState {
    object Loading : MessagingHomeUiState()
    data class Success(val conversations: List<ConversationItemScreen>) : MessagingHomeUiState()
    data class Empty(@param:StringRes val messageRes: Int = R.string.empty_messaging
    ) : MessagingHomeUiState()

    sealed class Error : MessagingHomeUiState() {
        data class Generic(@param:StringRes val messageRes: Int = R.string.error_generic) : Error()
    }
}