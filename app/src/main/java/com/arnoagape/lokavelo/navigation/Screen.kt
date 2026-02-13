package com.arnoagape.lokavelo.navigation

sealed interface Screen {

    // ACCOUNT
    sealed interface Account : Screen {
        data object AccountHome : Account
        data object Profile : Account
        sealed interface Settings : Screen {
            data object HelpSettings : Settings
            data object HomeSettings : Settings
            data object InfoSettings : Settings
            data object NotificationsSettings : Settings
            data object PaymentSettings : Settings
            data object VersionSettings : Settings
        }
    }

    // HOME
    sealed interface Main : Screen {
        data object Home : Main
        data object DetailPublicBike : Main
        data object PublicProfile : Main
        data object Contact : Main
    }

    // LOGIN
    data object Login : Screen

    // MESSAGING
    sealed interface Messaging : Screen {
        data object MessagingHome : Messaging
        data object MessagingDetail : Messaging
    }

    // OWNER
    sealed interface Owner : Screen {
        data object AddBike : Owner
        data object DetailBike : Owner
        data object EditBike : Owner
        data object HomeBike : Owner
    }

    // RENT
    data object Rent : Screen
}