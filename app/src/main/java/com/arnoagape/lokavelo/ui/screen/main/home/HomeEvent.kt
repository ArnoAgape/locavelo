package com.arnoagape.lokavelo.ui.screen.main.home

/**
 * Represents one-time UI events sent from ViewModels to the UI layer.
 *
 * These events are transient actions such as showing messages or
 * triggering navigation, and are not part of the persistent UI state.
 */
sealed interface HomeEvent {

    /**
     * Shows a message to the user using a string resource.
     */
    data class ShowMessage(val message: Int) : HomeEvent

    /**
     * Shows a success message to the user using a string resource.
     */
    data class ShowSuccessMessage(val message: Int) : HomeEvent
}
