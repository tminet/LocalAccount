package tmidev.localaccount.ui.screen.signup

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
import tmidev.localaccount.domain.model.User
import tmidev.localaccount.domain.model.type.InputError
import tmidev.localaccount.domain.model.type.InputResult
import tmidev.localaccount.domain.usecase.InsertUserUseCase
import tmidev.localaccount.domain.usecase.IsEmailAvailableUseCase
import tmidev.localaccount.domain.usecase.SetCurrentUserIdUseCase
import tmidev.localaccount.domain.usecase.ValidateEmailFieldUseCase
import tmidev.localaccount.domain.usecase.ValidateSimpleFieldUseCase
import tmidev.localaccount.util.md5
import javax.inject.Inject

/**
 * Sealed interface that represents one time events from view model to screen.
 */
sealed interface SignUpChannel {
    data object UnavailableEmail : SignUpChannel
    data object SignUpFailed : SignUpChannel
    data object SignUpSuccessfully : SignUpChannel
}

/**
 * Data class that represents the state of screen.
 */
data class SignUpScreenState(
    val name: String,
    val nameErrorResId: Int?,
    val email: String,
    val emailErrorResId: Int?,
    val password: String,
    val passwordErrorResId: Int?,
    val isPasswordVisible: Boolean
)

/**
 * Data class that represents the state of the view model.
 */
private data class SignUpViewModelState(
    val name: String = "",
    val nameErrorResId: Int? = null,
    val email: String = "",
    val emailErrorResId: Int? = null,
    val password: String = "",
    val passwordErrorResId: Int? = null,
    val isPasswordVisible: Boolean = false
) {
    fun asScreenState() = SignUpScreenState(
        name = name,
        nameErrorResId = nameErrorResId,
        email = email,
        emailErrorResId = emailErrorResId,
        password = password,
        passwordErrorResId = passwordErrorResId,
        isPasswordVisible = isPasswordVisible
    )
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val validateSimpleFieldUseCase: ValidateSimpleFieldUseCase,
    private val validateEmailFieldUseCase: ValidateEmailFieldUseCase,
    private val isEmailAvailableUseCase: IsEmailAvailableUseCase,
    private val insertUserUseCase: InsertUserUseCase,
    private val setCurrentUserIdUseCase: SetCurrentUserIdUseCase
) : ViewModel() {
    private val viewModelState = MutableStateFlow(value = SignUpViewModelState())

    val screenState = viewModelState.map { it.asScreenState() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = viewModelState.value.asScreenState()
    )

    private val _channel = Channel<SignUpChannel>()
    val channel = _channel.receiveAsFlow()

    /**
     * Update the name of User.
     *
     * @param value the string that represents the name of user.
     */
    fun changeName(value: String) {
        viewModelState.update { it.copy(name = value) }
    }

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
     * Starts the validation to create an account on local database.
     */
    fun signUp() {
        viewModelState.update { it.copy(name = it.name.trim()) }

        val nameResult = validateSimpleFieldUseCase(
            string = viewModelState.value.name
        )

        val emailResult = validateEmailFieldUseCase(
            string = viewModelState.value.email
        )

        val passwordResult = validateSimpleFieldUseCase(
            string = viewModelState.value.password,
            minChar = 4
        )

        viewModelState.update { state ->
            state.copy(
                nameErrorResId = when (nameResult) {
                    InputResult.Success -> null
                    is InputResult.Error -> when (nameResult.inputError) {
                        InputError.FieldEmpty -> R.string.nameEmptyError
                        else -> null
                    }
                },
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
                        InputError.FieldLessMinCharacters -> R.string.passwordTooShortError
                        else -> null
                    }
                }
            )
        }

        listOf(
            nameResult,
            emailResult,
            passwordResult
        ).any { inputResult ->
            inputResult is InputResult.Error
        }.also { hasError ->
            if (hasError) return
        }

        viewModelScope.launch {
            val isEmailAvailable = isEmailAvailableUseCase(email = viewModelState.value.email)
            if (!isEmailAvailable) {
                _channel.send(element = SignUpChannel.UnavailableEmail)
                return@launch
            }

            val user = User(
                name = viewModelState.value.name,
                email = viewModelState.value.email,
                password = viewModelState.value.password.md5(),
            )

            val userId = insertUserUseCase(user = user)
            if (userId > 0) {
                setCurrentUserIdUseCase(userId = userId, stayConnected = false)
                _channel.send(element = SignUpChannel.SignUpSuccessfully)
            } else _channel.send(element = SignUpChannel.SignUpFailed)
        }
    }
}