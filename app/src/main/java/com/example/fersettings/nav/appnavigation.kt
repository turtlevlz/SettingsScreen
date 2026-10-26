package com.example.fersettings.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fersettings.screen.NotificationsRoute
import com.example.fersettings.screen.SettingsRoute

private object Routes {
    const val SETTINGS = "settings"
    const val NOTIFICATIONS = "notifications"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SETTINGS,
    ) {
        composable(Routes.SETTINGS) {
            SettingsRoute(
                onNotificationsClick = {
                    navController.navigate(Routes.NOTIFICATIONS) {
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(Routes.NOTIFICATIONS) {
            NotificationsRoute(
                onBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}
