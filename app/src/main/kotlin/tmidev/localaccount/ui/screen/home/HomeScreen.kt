package tmidev.localaccount.ui.screen.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tmidev.localaccount.R
import tmidev.localaccount.util.LaIcons

/**
 * Compose the Home Screen.
 *
 * @param modifier the [Modifier] to apply on container of this screen.
 * @param windowInsets the [WindowInsets] to apply on container of this screen.
 * @param onNavigateBack callback to navigate back from this screen.
 * @param onNavigateToSignIn callback to navigate to sign in screen.
 * @param onNavigateToSettings callback to navigate to settings screen.
 * @param viewModel the [HomeViewModel]. Default is provided by [hiltViewModel].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets,
    onNavigateBack: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.channel.collect { channel ->
            when (channel) {
                HomeChannel.Logout -> onNavigateToSignIn()
            }
        }
    }

    BackHandler(onBack = onNavigateBack)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.appName)) },
                actions = {
                    IconButton(onClick = viewModel::logout) {
                        Icon(
                            imageVector = LaIcons.Logout,
                            contentDescription = stringResource(id = R.string.logout)
                        )
                    }

                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = LaIcons.Settings,
                            contentDescription = stringResource(id = R.string.settings)
                        )
                    }
                }
            )
        },
        contentWindowInsets = windowInsets
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.welcomeUser, screenState.userName))
        }
    }
}