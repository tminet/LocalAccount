package tmidev.localaccount.ui.screen.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tmidev.localaccount.R
import tmidev.localaccount.domain.model.type.InputError
import tmidev.localaccount.domain.model.type.InputResult
import tmidev.localaccount.domain.usecase.GetUserByEmailUseCase
import tmidev.localaccount.domain.usecase.SetCurrentUserIdUseCase
import tmidev.localaccount.domain.usecase.ValidateEmailFieldUseCase
import tmidev.localaccount.domain.usecase.ValidateSimpleFieldUseCase
import tmidev.localaccount.util.md5
import javax.inject.Inject

/**
 * Sealed interface that represents one time events from view model to screen.
 */
sealed interface SignInChannel {
    data object EmailNotFound : SignInChannel
    data object IncorrectPassword : SignInChannel
    data object SignInSuccessfully : SignInChannel
}

/**
 * Data class that represents the state of screen.
 */
data class SignInScreenState(
    val email: String,
    val emailErrorResId: Int?,
    val password: String,
    val passwordErrorResId: Int?,
    val isPasswordVisible: Boolean,
    val keepLogged: Boolean
)

/**
 * Data class that represents the state of the view model.
 */
private data class SignInViewModelState(
    val email: String = "",
    val emailErrorResId: Int? = null,
    val password: String = "",
    val passwordErrorResId: Int? = null,
    val isPasswordVisible: Boolean = false,
    val keepLogged: Boolean = false
) {
    fun asScreenState() = SignInScreenState(
        email = email,
        emailErrorResId = emailErrorResId,
        password = password,
        passwordErrorResId = passwordErrorResId,
        isPasswordVisible = isPasswordVisible,
        keepLogged = keepLogged
    )
}

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val validateSimpleFieldUseCase: ValidateSimpleFieldUseCase,
    private val validateEmailFieldUseCase: ValidateEmailFieldUseCase,
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
    private val setCurrentUserIdUseCase: SetCurrentUserIdUseCase
) : ViewModel() {
    private val viewModelState = MutableStateFlow(value = SignInViewModelState())

    val screenState = viewModelState.map { it.asScreenState() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = viewModelState.value.asScreenState()
    )

    private val _channel = Channel<SignInChannel>()
    val channel = _channel.receiveAsFlow()

    /**
     * Update the email of User.
     *
     * @param value the string that represents the email of user.
     */
    fun changeEmail(value: String) {
        viewModelState.update { it.copy(email = value) }
    }

    /**
     * Update the password of User.
     *
     * @param value the string that represents the name of user.
     */
    fun changePassword(value: String) {
        viewModelState.update { it.copy(password = value) }
    }

    /**
     * Toggle the characters visibility from password input.
     */
    fun togglePasswordVisibility() {
        viewModelState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    /**
     * Toggle the "keep logged" option.
     */
    fun toggleKeepLogged() {
        viewModelState.update { it.copy(keepLogged = !it.keepLogged) }
    }

    /**
     * Starts the validation to login with email and password.
     */
    fun signIn() {
        val emailResult = validateEmailFieldUseCase(string = viewModelState.value.email)
        val passwordResult = validateSimpleFieldUseCase(string = viewModelState.value.password)

        viewModelState.update { state ->
            state.copy(
                emailErrorResId = when (emailResult) {
                    InputResult.Success -> null
                    is InputResult.Error -> when (emailResult.inputError) {
                        InputError.FieldEmpty -> R.string.emailEmptyError
                        InputError.FieldInvalid -> R.string.emailInvalidError
                        else -> null
                    }
                },
                passwordErrorResId = when (passwordResult) {
                    InputResult.Success -> null
                    is InputResult.Error -> when (passwordResult.inputError) {
                        InputError.FieldEmpty -> R.string.passwordEmptyError
                        else -> null
                    }
                }
            )
        }

        listOf(
            emailResult,
            passwordResult
        ).any { inputResult ->
            inputResult is InputResult.Error
        }.also { hasError ->
            if (hasError) return
        }

        viewModelScope.launch {
            val userResult = getUserByEmailUseCase(email = viewModelState.value.email)
            if (userResult == null) {
                _channel.send(element = SignInChannel.EmailNotFound)
                return@launch
            }

            val isValidPassword = userResult.password == viewModelState.value.password.md5()
            if (!isValidPassword) {
                _channel.send(element = SignInChannel.IncorrectPassword)
                return@launch
            }

            setCurrentUserIdUseCase(
                userId = userResult.id,
                stayConnected = viewModelState.value.keepLogged
            )

            _channel.send(element = SignInChannel.SignInSuccessfully)
        }
    }
}