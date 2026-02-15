package com.arnoagape.lokavelo.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.arnoagape.lokavelo.R

sealed class BottomNavItem(
    val route: String,
    @param:StringRes val labelRes: Int,
    val icon: ImageVector
) {
    object Rent : BottomNavItem(
        route = "rent",
        labelRes = R.string.rent,
        icon = Icons.Default.Search
    )

    object Rentals : BottomNavItem(
        route = "rentals",
        labelRes = R.string.rentals,
        icon = Icons.AutoMirrored.Filled.DirectionsBike
    )

    object Messaging : BottomNavItem(
        route = "messaging",
        labelRes = R.string.messaging,
        icon = Icons.AutoMirrored.Filled.Message
    )

    object Account : BottomNavItem(
        route = "account",
        labelRes = R.string.account,
        icon = Icons.Default.Person
    )
}