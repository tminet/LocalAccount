package tmidev.localaccount.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tmidev.localaccount.domain.model.type.ThemeStyle
import tmidev.localaccount.domain.usecase.AppConfigurationStreamUseCase
import tmidev.localaccount.domain.usecase.GetCurrentUserIdUseCase
import tmidev.localaccount.domain.usecase.IsStayConnectedEnabledUseCase
import javax.inject.Inject

/**
 * Data class that represents the state of activity.
 */
data class MainActivityState(
    val isLoading: Boolean,
    val useDynamicColors: Boolean,
    val themeStyle: ThemeStyle,
    val isStayConnectedEnabled: Boolean,
    val currentUserId: Int,
)

/**
 * Data class that represents the state of the view model.
 */
private data class MainActivityViewModelState(
    val isLoading: Boolean = true,
    val useDynamicColors: Boolean = true,
    val themeStyle: ThemeStyle = ThemeStyle.FollowAndroidSystem,
    val isStayConnectedEnabled: Boolean = false,
    val currentUserId: Int = 0
) {
    fun asActivityState() = MainActivityState(
        isLoading = isLoading,
        useDynamicColors = useDynamicColors,
        themeStyle = themeStyle,
        isStayConnectedEnabled = isStayConnectedEnabled,
        currentUserId = currentUserId
    )
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val appConfigurationStreamUseCase: AppConfigurationStreamUseCase,
    private val isStayConnectedEnabledUseCase: IsStayConnectedEnabledUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {
    private val viewModelState = MutableStateFlow(value = MainActivityViewModelState())

    val activityState = viewModelState.map { it.asActivityState() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = viewModelState.value.asActivityState()
    )

    init {
        watchAppConfigurationStream()
    }

    private fun watchAppConfigurationStream() {
        viewModelScope.launch {
            viewModelState.update { it.copy(isLoading = true) }

            val isStayConnectedEnabled = isStayConnectedEnabledUseCase()
            val currentUserId = if (isStayConnectedEnabled) getCurrentUserIdUseCase() else 0

            delay(timeMillis = 2000)

            appConfigurationStreamUseCase().collectLatest { appConfiguration ->
                viewModelState.update { state ->
                    state.copy(
                        isLoading = false,
                        useDynamicColors = appConfiguration.useDynamicColors,
                        themeStyle = appConfiguration.themeStyle,
                        isStayConnectedEnabled = isStayConnectedEnabled,
                        currentUserId = currentUserId
                    )
                }
            }
        }
    }
}