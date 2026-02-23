package com.arnoagape.lokavelo.navigation

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arnoagape.lokavelo.R
import com.arnoagape.lokavelo.ui.screen.account.home.AccountHomeScreen
import com.arnoagape.lokavelo.ui.screen.account.profile.ProfileScreen
import com.arnoagape.lokavelo.ui.screen.account.settings.help.HelpSettingsScreen
import com.arnoagape.lokavelo.ui.screen.account.settings.home.HomeSettingsScreen
import com.arnoagape.lokavelo.ui.screen.account.settings.info.InfoSettingsScreen
import com.arnoagape.lokavelo.ui.screen.account.settings.notifications.NotificationsSettingsScreen
import com.arnoagape.lokavelo.ui.screen.account.settings.payment.PaymentSettingsScreen
import com.arnoagape.lokavelo.ui.screen.account.settings.version.VersionSettingsScreen
import com.arnoagape.lokavelo.ui.screen.login.LoginScreen
import com.arnoagape.lokavelo.ui.screen.login.LoginViewModel
import com.arnoagape.lokavelo.ui.screen.main.contact.ContactScreen
import com.arnoagape.lokavelo.ui.screen.main.detail.DetailPublicBikeScreen
import com.arnoagape.lokavelo.ui.screen.main.home.HomeScreen
import com.arnoagape.lokavelo.ui.screen.main.profile.PublicProfileScreen
import com.arnoagape.lokavelo.ui.screen.messaging.detail.MessagingDetailScreen
import com.arnoagape.lokavelo.ui.screen.messaging.home.MessagingHomeScreen
import com.arnoagape.lokavelo.ui.screen.owner.addBike.AddBikeScreen
import com.arnoagape.lokavelo.ui.screen.owner.addBike.AddBikeViewModel
import com.arnoagape.lokavelo.ui.screen.owner.detail.DetailBikeScreen
import com.arnoagape.lokavelo.ui.screen.owner.detail.DetailBikeViewModel
import com.arnoagape.lokavelo.ui.screen.owner.editBike.EditBikeScreen
import com.arnoagape.lokavelo.ui.screen.owner.editBike.EditBikeViewModel
import com.arnoagape.lokavelo.ui.screen.owner.home.HomeBikeScreen
import com.arnoagape.lokavelo.ui.screen.owner.home.HomeBikeViewModel
import com.arnoagape.lokavelo.ui.screen.rent.RentScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LokaveloApp() {

    val navController = rememberNavController()

    val loginViewModel: LoginViewModel = hiltViewModel()
    val isSignedIn by loginViewModel.isSignedIn.collectAsStateWithLifecycle()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    var lastBackPressedTime by remember { mutableLongStateOf(0L) }
    val context = LocalContext.current
    val resources = LocalResources.current

    // appuie deux fois pour quitter
    BackHandler {

        val currentRoute =
            currentBackStackEntry?.destination?.route?.substringBefore("/")

        val isOnRootScreen =
            currentRoute == Screen.Owner.HomeBike.route ||
                    currentRoute == Screen.Rent.route ||
                    currentRoute == Screen.Account.AccountHome.route ||
                    currentRoute == Screen.Messaging.MessagingHome.route

        if (!isOnRootScreen) {
            navController.popBackStack()
        } else {
            val currentTime = System.currentTimeMillis()

            if (currentTime - lastBackPressedTime < 2000) {
                (context as? Activity)?.finish()
            } else {
                lastBackPressedTime = currentTime
                Toast.makeText(
                    context,
                    resources.getString(R.string.press_again_exit),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun navigateProtected(route: String) {
        if (isSignedIn) {
            navController.navigate(route)
        } else {
            navController.navigate(Screen.Login.route) {
                popUpTo(0)
            }
        }
    }

    Scaffold(

        bottomBar = {

            val currentRoute = currentBackStackEntry?.destination?.route
            val currentScreen = screenFromRoute(currentRoute)

            val hideBottomBar =
                currentScreen is Screen.Owner.DetailBike ||
                        currentScreen is Screen.Owner.EditBike ||
                        currentScreen is Screen.Owner.AddBike ||
                        currentScreen is Screen.Login

            if (!hideBottomBar && currentScreen != null) {

                BottomBar(
                    currentScreen = currentScreen,
                    onItemSelected = { screen ->

                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }

    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Owner.HomeBike.route,
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
        ) {

            // ---------------- LOGIN ----------------

            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Owner.HomeBike.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            // ---------------- OWNER ----------------

            composable(Screen.Owner.HomeBike.route) {

                val vm: HomeBikeViewModel = hiltViewModel()

                HomeBikeScreen(
                    viewModel = vm,
                    onBikeClick = { bike ->
                        navController.navigate(
                            Screen.Owner.DetailBike.createRoute(bike.id)
                        )
                    },
                    onAddBikeClick = { navigateProtected(Screen.Owner.AddBike.route) }
                )
            }

            composable(Screen.Owner.AddBike.route) {

                val vm: AddBikeViewModel = hiltViewModel()

                AddBikeScreen(
                    viewModel = vm,
                    onSaveClick = {
                        navController.popBackStack()
                    },
                    onClose = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.Owner.DetailBike.route,
                arguments = listOf(
                    navArgument("bikeId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val bikeId =
                    backStackEntry.arguments?.getString("bikeId")!!

                val vm: DetailBikeViewModel = hiltViewModel()

                DetailBikeScreen(
                    bikeId = bikeId,
                    viewModel = vm,
                    onBikeDeleted = {
                        navController.popBackStack()
                    },
                    onEditClick = {
                        navController.navigate(
                            Screen.Owner.EditBike.createRoute(bikeId)
                        )
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.Owner.EditBike.route,
                arguments = listOf(
                    navArgument("bikeId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val bikeId =
                    backStackEntry.arguments?.getString("bikeId")!!

                val vm: EditBikeViewModel = hiltViewModel()

                EditBikeScreen(
                    bikeId = bikeId,
                    viewModel = vm,
                    onSaveClick = {
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            // ---------------- ACCOUNT ----------------

            composable(Screen.Account.AccountHome.route) {
                AccountHomeScreen()
            }

            composable(Screen.Account.Profile.route) {
                ProfileScreen()
            }

            composable(Screen.Account.Settings.HelpSettings.route) {
                HelpSettingsScreen()
            }

            composable(Screen.Account.Settings.HomeSettings.route) {
                HomeSettingsScreen()
            }

            composable(Screen.Account.Settings.InfoSettings.route) {
                InfoSettingsScreen()
            }

            composable(Screen.Account.Settings.NotificationsSettings.route) {
                NotificationsSettingsScreen()
            }

            composable(Screen.Account.Settings.PaymentSettings.route) {
                PaymentSettingsScreen()
            }

            composable(Screen.Account.Settings.VersionSettings.route) {
                VersionSettingsScreen()
            }

            // ---------------- MAIN ----------------

            composable(Screen.Main.Home.route) {
                HomeScreen()
            }

            composable(Screen.Main.Contact.route) {
                ContactScreen()
            }

            composable(Screen.Main.DetailPublicBike.route) {
                DetailPublicBikeScreen()
            }

            composable(Screen.Main.PublicProfile.route) {
                PublicProfileScreen()
            }

            // ---------------- MESSAGING ----------------

            composable(Screen.Messaging.MessagingHome.route) {
                MessagingHomeScreen()
            }

            composable(Screen.Messaging.MessagingDetail.route) {
                MessagingDetailScreen()
            }

            // ---------------- RENT ----------------

            composable(Screen.Rent.route) {
                RentScreen()
            }
        }
    }
}

fun screenFromRoute(route: String?): Screen? {
    return when (route?.substringBefore("/")) {

        Screen.Rent.route -> Screen.Rent
        Screen.Owner.HomeBike.route -> Screen.Owner.HomeBike
        Screen.Account.AccountHome.route -> Screen.Account.AccountHome
        Screen.Messaging.MessagingHome.route -> Screen.Messaging.MessagingHome
        Screen.Login.route -> Screen.Login

        Screen.Owner.AddBike.route -> Screen.Owner.AddBike

        "owner_detail" -> Screen.Owner.DetailBike
        "owner_edit" -> Screen.Owner.EditBike

        else -> null
    }
}