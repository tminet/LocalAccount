package tmidev.localaccount.ui.screen.signin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tmidev.localaccount.R
import tmidev.localaccount.ui.component.LaButton
import tmidev.localaccount.ui.component.LaOutlinedTextField
import tmidev.localaccount.ui.component.LaTextButton
import tmidev.localaccount.util.ClearTrailingButton
import tmidev.localaccount.util.LaIcons
import tmidev.localaccount.util.ToggleTextVisibilityTrailingButton
import tmidev.localaccount.util.safeStringResource

/**
 * Compose the Sign In Screen.
 *
 * @param modifier the [Modifier] to apply on container of this screen.
 * @param windowInsets the [WindowInsets] to apply on container of this screen.
 * @param onNavigateBack callback to navigate back from this screen.
 * @param onNavigateToHome callback to navigate to home screen.
 * @param onNavigateToSignUp callback to navigate to sign up screen.
 * @param onNavigateToResetPassword callback to navigate to reset password screen.
 * @param viewModel the [SignInViewModel]. Default is provided by [hiltViewModel].
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToResetPassword: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = Unit) {
        viewModel.channel.collect { channel ->
            when (channel) {
                SignInChannel.EmailNotFound -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.emailNotAssociatedWithAccount),
                    withDismissAction = true
                )
                SignInChannel.IncorrectPassword -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.incorrectPassword),
                    withDismissAction = true
                )
                SignInChannel.SignInSuccessfully -> onNavigateToHome()
            }
        }
    }

    BackHandler(onBack = onNavigateBack)

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.localAccountSignIn)) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = windowInsets
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .verticalScroll(state = scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LaOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = screenState.email,
                onValueChange = { viewModel.changeEmail(value = it) },
                labelString = stringResource(id = R.string.email),
                placeholderString = stringResource(id = R.string.emailPlaceholder),
                errorMessage = safeStringResource(id = screenState.emailErrorResId),
                leadingIcon = {
                    Icon(imageVector = LaIcons.Email, contentDescription = null)
                },
                trailingIcon = if (screenState.email.isEmpty()) null else {
                    {
                        ClearTrailingButton(
                            onClick = { viewModel.changeEmail(value = "") }
                        )
                    }
                },
                enableWhiteSpace = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            LaOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = screenState.password,
                onValueChange = { viewModel.changePassword(value = it) },
                labelString = stringResource(id = R.string.password),
                placeholderString =
                if (screenState.isPasswordVisible) stringResource(id = R.string.passwordPlaceholderShow)
                else stringResource(id = R.string.passwordPlaceholderHide),
                errorMessage = safeStringResource(id = screenState.passwordErrorResId),
                leadingIcon = {
                    Icon(imageVector = LaIcons.Password, contentDescription = null)
                },
                trailingIcon = {
                    ToggleTextVisibilityTrailingButton(
                        onClick = viewModel::togglePasswordVisibility,
                        isVisible = screenState.isPasswordVisible
                    )
                },
                enableWhiteSpace = false,
                hideText = !screenState.isPasswordVisible,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        viewModel.signIn()
                    }
                )
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(alignment = Alignment.Start)
                    .toggleable(
                        value = screenState.keepLogged,
                        role = Role.Checkbox,
                        onValueChange = { viewModel.toggleKeepLogged() }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    modifier = Modifier.padding(all = 4.dp),
                    checked = screenState.keepLogged,
                    onCheckedChange = null
                )

                Text(
                    modifier = Modifier.padding(all = 4.dp),
                    text = stringResource(id = R.string.stayConnected),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.weight(weight = 1F))

                LaTextButton(
                    text = stringResource(id = R.string.forgotPassword),
                    onClick = onNavigateToResetPassword
                )
            }

            Spacer(modifier = Modifier.height(height = 16.dp))

            LaButton(
                text = stringResource(id = R.string.signIn),
                onClick = viewModel::signIn
            )

            Spacer(modifier = Modifier.height(height = 32.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(id = R.string.dontHaveAccount))

                LaTextButton(
                    text = stringResource(id = R.string.signUp),
                    onClick = onNavigateToSignUp
                )
            }

            Spacer(modifier = Modifier.height(height = 16.dp))
        }
    }
}