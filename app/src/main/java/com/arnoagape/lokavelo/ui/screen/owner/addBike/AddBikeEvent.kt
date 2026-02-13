package com.arnoagape.lokavelo.ui.screen.owner.addBike

import android.net.Uri
import com.arnoagape.lokavelo.domain.model.BikeCategory
import com.arnoagape.lokavelo.domain.model.BikeEquipment
import com.arnoagape.lokavelo.domain.model.BikeState

/**
 * A sealed class representing different events that can occur on a form.
 */
sealed interface AddBikeEvent {

    data class TitleChanged(val title: String) : AddBikeEvent
    data class DescriptionChanged(val description: String) : AddBikeEvent
    data class LocationChanged(val location: String) : AddBikeEvent
    data class PriceChanged(val price: Long) : AddBikeEvent
    data class DepositChanged(val deposit: Long) : AddBikeEvent
    data class ElectricChanged(val isElectric: Boolean) : AddBikeEvent
    data class CategoryChanged(val category: BikeCategory) : AddBikeEvent
    data class StateChanged(val state: BikeState) : AddBikeEvent
    data class AccessoriesChanged(val accessories: List<BikeEquipment>) : AddBikeEvent
    data class AddPhoto(val uri: Uri) : AddBikeEvent
    data class RemovePhoto(val uri: Uri) : AddBikeEvent
    data object Submit : AddBikeEvent
}
