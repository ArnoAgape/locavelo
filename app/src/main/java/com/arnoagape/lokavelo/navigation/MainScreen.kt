package com.arnoagape.lokavelo.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arnoagape.lokavelo.ui.screen.account.home.AccountHomeScreen
import com.arnoagape.lokavelo.ui.screen.messaging.home.MessagingHomeScreen
import com.arnoagape.lokavelo.ui.screen.owner.home.HomeBikeScreen
import com.arnoagape.lokavelo.ui.screen.rent.RentScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Rent.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(BottomNavItem.Rent.route) {
                RentScreen()
            }

            composable(BottomNavItem.Rentals.route) {
                HomeBikeScreen()
            }

            composable(BottomNavItem.Messaging.route) {
                MessagingHomeScreen()
            }

            composable(BottomNavItem.Account.route) {
                AccountHomeScreen()
            }
        }
    }
}