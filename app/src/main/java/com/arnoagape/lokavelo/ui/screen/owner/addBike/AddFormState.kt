package com.arnoagape.lokavelo.ui.screen.owner.addBike

import android.net.Uri
import com.arnoagape.lokavelo.domain.model.Bike
import com.arnoagape.lokavelo.domain.model.BikeCategory
import com.arnoagape.lokavelo.domain.model.BikeCondition
import com.arnoagape.lokavelo.domain.model.BikeEquipment
import kotlin.collections.isNotEmpty

data class AddFormState(
    val title: String = "",
    val description: String = "",
    val location: String = "",
    val priceText: String = "",
    val depositText: String = "",
    val isElectric: Boolean = false,
    val category: BikeCategory? = null,
    val brand: String = "",
    val condition: BikeCondition? = null,
    val accessories: List<BikeEquipment> = emptyList()
) {
    fun isValid(uris: List<Uri>): Boolean {

        val price = priceText
            .replace(",", ".")
            .toDoubleOrNull()

        return title.isNotBlank() &&
                location.isNotBlank() &&
                price != null &&
                price > 0 &&
                uris.isNotEmpty()
    }

    fun toBikeOrNull(): Bike? {

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

        return Bike(
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