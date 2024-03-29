package tmidev.localaccount.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import tmidev.localaccount.ui.component.laWindowInsets
import tmidev.localaccount.ui.component.theme.LaTheme
import tmidev.localaccount.ui.component.theme.LaThemeSplash
import tmidev.localaccount.ui.navigation.TopLevelNavHost
import tmidev.localaccount.ui.navigation.TopLevelScreen
import tmidev.localaccount.util.shouldUseDarkTheme
import tmidev.localaccount.util.transparentEdge

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MainActivityViewModel = hiltViewModel()
            val windowsInsets = laWindowInsets()
            val activityState by viewModel.activityState.collectAsStateWithLifecycle()

            val shouldUseDarkTheme = shouldUseDarkTheme(themeStyle = activityState.themeStyle)

            DisposableEffect(key1 = shouldUseDarkTheme) {
                transparentEdge(darkMode = shouldUseDarkTheme)
                onDispose { }
            }

            when (activityState.isLoading) {
                true -> LaThemeSplash()
                false -> LaTheme(
                    useDarkTheme = shouldUseDarkTheme,
                    useDynamicColors = activityState.useDynamicColors
                ) {
                    TopLevelNavHost(
                        modifier = Modifier.fillMaxSize(),
                        windowInsets = windowsInsets,
                        onNavigateBack = { moveTaskToBack(true) },
                        startDestination = startDestination(
                            isStayConnectedEnabled = activityState.isStayConnectedEnabled,
                            currentUserId = activityState.currentUserId
                        )
                    )
                }
            }
        }
    }

    private fun startDestination(
        isStayConnectedEnabled: Boolean,
        currentUserId: Int
    ): String = if (isStayConnectedEnabled && currentUserId > 0)
        TopLevelScreen.Home.route else TopLevelScreen.SignIn.route
}