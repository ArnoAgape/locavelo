package com.arnoagape.lokavelo.ui.screen.owner.editBike

import com.arnoagape.lokavelo.domain.model.Bike
import com.arnoagape.lokavelo.domain.model.BikeCategory
import com.arnoagape.lokavelo.domain.model.BikeCondition
import com.arnoagape.lokavelo.domain.model.BikeEquipment
import com.arnoagape.lokavelo.domain.model.BikeLocation

data class EditBikeFormState(
    val title: String = "",
    val description: String = "",
    val location: BikeLocation = BikeLocation(),
    val priceText: String = "",
    val depositText: String = "",
    val isElectric: Boolean = false,
    val category: BikeCategory? = null,
    val brand: String = "",
    val condition: BikeCondition? = null,
    val accessories: List<BikeEquipment> = emptyList()
) {
    fun isValid(totalPhotos: Int): Boolean {
        return title.isNotBlank() &&
                priceText.isNotBlank() &&
                location.street.isNotBlank() &&
                location.city.isNotBlank() &&
                location.postalCode.isNotBlank() &&
                totalPhotos > 0
    }

    fun toUpdatedBikeOrNull(original: Bike): Bike? {

        val price = priceText
            .replace(",", ".")
            .toDoubleOrNull()
            ?.times(100)
            ?.toLong()
            ?: return null

        val deposit = depositText
            .replace(",", ".")
            .toDoubleOrNull()
            ?.times(100)
            ?.toLong()

        if (price <= 0) return null

        return original.copy(
            title = title,
            description = description,
            location = location,
            priceInCents = price,
            depositInCents = deposit,
            isElectric = isElectric,
            category = category,
            brand = brand,
            condition = condition,
            accessories = accessories
        )
    }

}