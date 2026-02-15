package com.arnoagape.lokavelo.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomBar(
    currentScreen: Screen,
    onItemSelected: (Screen) -> Unit
) {
    NavigationBar {

        NavigationBarItem(
            selected = currentScreen is Screen.Rent,
            onClick = { onItemSelected(Screen.Rent) },
            icon = { Icon(Icons.Default.Search, null) },
            label = { Text("Louer") }
        )

        NavigationBarItem(
            selected = currentScreen is Screen.Owner.HomeBike,
            onClick = { onItemSelected(Screen.Owner.HomeBike) },
            icon = { Icon(Icons.AutoMirrored.Filled.DirectionsBike, null) },
            label = { Text("Locations") }
        )

        NavigationBarItem(
            selected = currentScreen is Screen.Messaging.MessagingHome,
            onClick = { onItemSelected(Screen.Messaging.MessagingHome) },
            icon = { Icon(Icons.AutoMirrored.Filled.Message, null) },
            label = { Text("Messagerie") }
        )

        NavigationBarItem(
            selected = currentScreen is Screen.Account.AccountHome,
            onClick = { onItemSelected(Screen.Account.AccountHome) },
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Compte") }
        )
    }
}