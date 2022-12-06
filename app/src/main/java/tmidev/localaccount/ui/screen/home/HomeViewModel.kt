package tmidev.localaccount.ui.screen.home

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
import tmidev.localaccount.domain.usecase.GetCurrentUserIdUseCase
import tmidev.localaccount.domain.usecase.GetUserByIdStreamUseCase
import tmidev.localaccount.domain.usecase.LogoutUseCase
import javax.inject.Inject

/**
 * Sealed interface that represents one time events from view model to screen.
 */
sealed interface HomeChannel {
    object Logout : HomeChannel
}

/**
 * Data class that represents the state of screen.
 */
data class HomeScreenState(
    val isLoading: Boolean,
    val userName: String
)

/**
 * Data class that represents the state of the view model.
 */
private data class HomeViewModelState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val userId: Int = 0
) {
    fun asScreenState() = HomeScreenState(
        isLoading = isLoading,
        userName = userName
    )
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getUserByIdStreamUseCase: GetUserByIdStreamUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val viewModelState = MutableStateFlow(value = HomeViewModelState())

    val screenState = viewModelState.map { it.asScreenState() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = viewModelState.value.asScreenState()
    )

    private val _channel = Channel<HomeChannel>()
    val channel = _channel.receiveAsFlow()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val currentUserId = getCurrentUserIdUseCase()
            getUserByIdStreamUseCase(id = currentUserId).collectLatest { user ->
                user?.let {
                    viewModelState.update { state ->
                        state.copy(
                            userId = user.id,
                            userName = user.name
                        )
                    }
                }
                viewModelState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _channel.send(element = HomeChannel.Logout)
        }
    }
}