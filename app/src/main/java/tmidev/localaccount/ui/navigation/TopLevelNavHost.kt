package tmidev.localaccount.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import tmidev.localaccount.ui.screen.home.HomeScreen
import tmidev.localaccount.ui.screen.settings.SettingsScreen
import tmidev.localaccount.ui.screen.signin.SignInScreen
import tmidev.localaccount.ui.screen.signup.SignUpScreen

/**
 * [NavHost] for top level screens.
 *
 * @param modifier the [Modifier] to apply on this nav host.
 * @param windowInsets the [WindowInsets] to apply on this nav host.
 * @param onNavigateBack callback to navigate back from this nav host.
 * @param startDestination the string route for start destination.
 * @param topLevelState the [TopLevelState] to be used as state. Default is [rememberTopLevelState].
 */
@Composable
fun TopLevelNavHost(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets,
    onNavigateBack: () -> Unit,
    startDestination: String,
    topLevelState: TopLevelState = rememberTopLevelState()
) = NavHost(
    modifier = modifier,
    navController = topLevelState.navController,
    route = "top_level_nav_host",
    startDestination = startDestination
) {
    composable(route = TopLevelScreen.SignIn.route) {
        SignInScreen(
            modifier = Modifier.fillMaxSize(),
            windowInsets = windowInsets,
            onNavigateBack = onNavigateBack,
            onNavigateToHome = {
                topLevelState.navigateAndClearBack(
                    currentRoute = TopLevelScreen.SignIn.route,
                    destinationRoute = TopLevelScreen.Home.route
                )
            },
            onNavigateToSignUp = {
                topLevelState.navigate(destinationRoute = TopLevelScreen.SignUp.route)
            }
        )
    }

    composable(route = TopLevelScreen.SignUp.route) {
        SignUpScreen(
            modifier = Modifier.fillMaxSize(),
            windowInsets = windowInsets,
            onNavigateBack = {
                topLevelState.navigateBackWithFallback(
                    currentRoute = TopLevelScreen.SignUp.route,
                    destinationRoute = TopLevelScreen.SignIn.route
                )
            },
            onNavigateToHome = {
                topLevelState.navigateAndClearBack(
                    currentRoute = TopLevelScreen.SignUp.route,
                    destinationRoute = TopLevelScreen.Home.route
                )
            }
        )
    }

    composable(route = TopLevelScreen.Home.route) {
        HomeScreen(
            modifier = Modifier.fillMaxSize(),
            windowInsets = windowInsets,
            onNavigateBack = onNavigateBack,
            onNavigateToSignIn = {
                topLevelState.navigateAndClearBack(
                    currentRoute = TopLevelScreen.Home.route,
                    destinationRoute = TopLevelScreen.SignIn.route
                )
            },
            onNavigateToSettings = {
                topLevelState.navigate(destinationRoute = TopLevelScreen.Settings.route)
            }
        )
    }

    composable(route = TopLevelScreen.Settings.route) {
        SettingsScreen(
            modifier = Modifier.fillMaxSize(),
            windowInsets = windowInsets,
            onNavigateBack = {
                topLevelState.navigateBackWithFallback(
                    currentRoute = TopLevelScreen.Settings.route,
                    destinationRoute = TopLevelScreen.Home.route
                )
            }
        )
    }
}

sealed class TopLevelScreen(val route: String) {
    object SignIn : TopLevelScreen(route = "sign_in")
    object SignUp : TopLevelScreen(route = "sign_up")
    object Home : TopLevelScreen(route = "home")
    object Settings : TopLevelScreen(route = "settings")
}