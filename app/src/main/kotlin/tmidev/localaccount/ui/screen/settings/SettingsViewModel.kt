package tmidev.localaccount.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tmidev.localaccount.R
import tmidev.localaccount.domain.model.User
import tmidev.localaccount.domain.model.type.InputError
import tmidev.localaccount.domain.model.type.InputResult
import tmidev.localaccount.domain.model.type.ThemeStyle
import tmidev.localaccount.domain.usecase.AppConfigurationStreamUseCase
import tmidev.localaccount.domain.usecase.ChangeThemeStyleUseCase
import tmidev.localaccount.domain.usecase.GetCurrentUserIdUseCase
import tmidev.localaccount.domain.usecase.GetUserByIdUseCase
import tmidev.localaccount.domain.usecase.IsEmailAvailableUseCase
import tmidev.localaccount.domain.usecase.ToggleDynamicColorsUseCase
import tmidev.localaccount.domain.usecase.UpdateUserUseCase
import tmidev.localaccount.domain.usecase.ValidateEmailFieldUseCase
import tmidev.localaccount.domain.usecase.ValidateSimpleFieldUseCase
import javax.inject.Inject

/**
 * Sealed interface that represents one time events from view model to screen.
 */
sealed interface SettingsChannel {
    data object UnavailableEmail : SettingsChannel
    data object AccountUpdated : SettingsChannel
}

/**
 * Data class that represents the state of screen.
 */
data class SettingsScreenState(
    val useDynamicColors: Boolean,
    val themeStyle: ThemeStyle,
    val userName: String,
    val userNameErrorResId: Int?,
    val userEmail: String,
    val userEmailErrorResId: Int?
)

/**
 * Data class that represents the state of the view model.
 */
private data class SettingsViewModelState(
    val useDynamicColors: Boolean = true,
    val themeStyle: ThemeStyle = ThemeStyle.FollowAndroidSystem,
    val userName: String = "",
    val userNameErrorResId: Int? = null,
    val userEmail: String = "",
    val userEmailErrorResId: Int? = null,
    val user: User? = null
) {
    fun asScreenState() = SettingsScreenState(
        useDynamicColors = useDynamicColors,
        themeStyle = themeStyle,
        userName = userName,
        userNameErrorResId = userNameErrorResId,
        userEmail = userEmail,
        userEmailErrorResId = userEmailErrorResId
    )
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appConfigurationStreamUseCase: AppConfigurationStreamUseCase,
    private val toggleDynamicColorsUseCase: ToggleDynamicColorsUseCase,
    private val changeThemeStyleUseCase: ChangeThemeStyleUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val isEmailAvailableUseCase: IsEmailAvailableUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val validateSimpleFieldUseCase: ValidateSimpleFieldUseCase,
    private val validateEmailFieldUseCase: ValidateEmailFieldUseCase
) : ViewModel() {
    private val viewModelState = MutableStateFlow(value = SettingsViewModelState())

    val screenState = viewModelState.map { it.asScreenState() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = viewModelState.value.asScreenState()
    )

    private val _channel = Channel<SettingsChannel>()
    val channel = _channel.receiveAsFlow()

    init {
        watchAppConfigurationStream()
        loadUserData()
    }

    private fun watchAppConfigurationStream() {
        viewModelScope.launch {
            appConfigurationStreamUseCase().collectLatest { appConfiguration ->
                viewModelState.update { state ->
                    state.copy(
                        useDynamicColors = appConfiguration.useDynamicColors,
                        themeStyle = appConfiguration.themeStyle
                    )
                }
            }
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val currentUserId = getCurrentUserIdUseCase()
            getUserByIdUseCase(id = currentUserId)?.let { user ->
                viewModelState.update { state ->
                    state.copy(
                        user = user,
                        userName = user.name,
                        userEmail = user.email
                    )
                }
            }
        }
    }

    /**
     * Toggle the DynamicColors option.
     */
    fun toggleDynamicColors() {
        viewModelScope.launch {
            toggleDynamicColorsUseCase()
        }
    }

    /**
     * Change the ThemeStyle.
     *
     * @param themeStyle the [ThemeStyle] to apply on app.
     */
    fun changeThemeStyle(themeStyle: ThemeStyle) {
        viewModelScope.launch {
            changeThemeStyleUseCase(themeStyle = themeStyle)
        }
    }

    /**
     * Update the name of User.
     *
     * @param value the string that represents the name of user.
     */
    fun changeName(value: String) {
        viewModelState.update { it.copy(userName = value) }
    }

    /**
     * Update the email of User.
     *
     * @param value the string that represents the email of user.
     */
    fun changeEmail(value: String) {
        viewModelState.update { it.copy(userEmail = value) }
    }

    /**
     * Save the current data of account.
     */
    fun saveAccountData() {
        viewModelState.update { it.copy(userName = it.userName.trim()) }

        val nameResult = validateSimpleFieldUseCase(
            string = viewModelState.value.userName
        )

        val emailResult = validateEmailFieldUseCase(
            string = viewModelState.value.userEmail
        )

        viewModelState.update { state ->
            state.copy(
                userNameErrorResId = when (nameResult) {
                    InputResult.Success -> null
                    is InputResult.Error -> when (nameResult.inputError) {
                        InputError.FieldEmpty -> R.string.nameEmptyError
                        else -> null
                    }
                },
                userEmailErrorResId = when (emailResult) {
                    InputResult.Success -> null
                    is InputResult.Error -> when (emailResult.inputError) {
                        InputError.FieldEmpty -> R.string.emailEmptyError
                        InputError.FieldInvalid -> R.string.emailInvalidError
                        else -> null
                    }
                }
            )
        }

        listOf(
            nameResult,
            emailResult
        ).any { inputResult ->
            inputResult is InputResult.Error
        }.also { hasError ->
            if (hasError) return
        }

        viewModelScope.launch {
            viewModelState.value.user?.let {
                if (it.email != viewModelState.value.userEmail) {
                    val isEmailAvailable = isEmailAvailableUseCase(
                        email = viewModelState.value.userEmail
                    )
                    if (!isEmailAvailable) {
                        _channel.send(element = SettingsChannel.UnavailableEmail)
                        return@launch
                    }
                }

                val user = it.copy(
                    name = viewModelState.value.userName,
                    email = viewModelState.value.userEmail
                )

                updateUserUseCase(user = user)
                _channel.send(element = SettingsChannel.AccountUpdated)
            }
        }
    }
}