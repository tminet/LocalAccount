package tmidev.localaccount.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

/**
 * Class to manage navigation of [TopLevelNavHost].
 */
@Stable
class TopLevelState(
    val navController: NavHostController
) {
    fun navigate(destinationRoute: String) {
        navController.navigate(route = destinationRoute) {
            launchSingleTop = true
        }
    }

    fun navigateAndClearBack(currentRoute: String, destinationRoute: String) {
        navController.navigate(route = destinationRoute) {
            popUpTo(route = currentRoute) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navigateBackWithFallback(currentRoute: String, destinationRoute: String) {
        if (!navController.popBackStack())
            navController.navigate(route = destinationRoute) {
                popUpTo(route = currentRoute) { inclusive = true }
                launchSingleTop = true
            }
    }
}

/**
 * Creates a remember state for [TopLevelState].
 */
@Composable
fun rememberTopLevelState(
    navController: NavHostController = rememberNavController()
): TopLevelState = remember(key1 = navController) {
    TopLevelState(navController = navController)
}