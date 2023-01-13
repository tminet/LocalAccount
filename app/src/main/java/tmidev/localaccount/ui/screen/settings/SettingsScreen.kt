package tmidev.localaccount.ui.screen.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tmidev.localaccount.R
import tmidev.localaccount.ui.screen.settings.sections.AccountSection
import tmidev.localaccount.ui.screen.settings.sections.AppearanceSection
import tmidev.localaccount.util.LaIcons

/**
 * Compose the Settings Screen.
 *
 * @param modifier the [Modifier] to apply on container of this screen.
 * @param windowInsets the [WindowInsets] to apply on container of this screen.
 * @param onNavigateBack callback to navigate back from this screen.
 * @param viewModel the [SettingsViewModel]. Default is provided by [hiltViewModel].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets,
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    BackHandler(onBack = onNavigateBack)

    LaunchedEffect(key1 = Unit) {
        viewModel.channel.collect { channel ->
            when (channel) {
                SettingsChannel.UnavailableEmail -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.emailNotAvailable),
                    withDismissAction = true
                )
                SettingsChannel.AccountUpdated -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.accountUpdated),
                    withDismissAction = true
                )
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = LaIcons.NavigateBefore,
                            contentDescription = stringResource(id = R.string.navigateBack)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = windowInsets
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .verticalScroll(state = scrollState)
        ) {
            AppearanceSection(
                modifier = Modifier.padding(horizontal = 16.dp),
                useDynamicColors = screenState.useDynamicColors,
                toggleDynamicColors = viewModel::toggleDynamicColors,
                themeStyle = screenState.themeStyle,
                changeThemeStyle = { viewModel.changeThemeStyle(themeStyle = it) }
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            AccountSection(
                modifier = Modifier.padding(horizontal = 16.dp),
                name = screenState.userName,
                nameErrorResId = screenState.userNameErrorResId,
                changeName = { viewModel.changeName(value = it) },
                email = screenState.userEmail,
                emailErrorResId = screenState.userEmailErrorResId,
                changeEmail = { viewModel.changeEmail(value = it) },
                saveAccountData = viewModel::saveAccountData
            )
        }
    }
}