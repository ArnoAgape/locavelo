package com.arnoagape.lokavelo.data.dto

import com.arnoagape.lokavelo.domain.model.BikeCategory
import com.arnoagape.lokavelo.domain.model.BikeEquipment
import com.arnoagape.lokavelo.domain.model.BikeCondition
import java.io.Serializable

data class BikeDto(
    val id: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val title: String = "",
    val description: String = "",
    val category: BikeCategory? = null,
    val brand: String = "",
    val state: BikeCondition? = null,
    val accessories: List<BikeEquipment> = emptyList(),
    val price: Long? = null,
    val deposit: Long? = null,
    val photoUrl: List<String> = emptyList(),
    val location: String = "",
    val ownerId: String = "",
    val isElectric: Boolean = false,
    val isAvailable: Boolean = true
) : Serializable