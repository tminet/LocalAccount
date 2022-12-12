package tmidev.localaccount.data.repository

import kotlinx.coroutines.flow.Flow
import tmidev.localaccount.data.datasource.datastore.LocalAccountPreferences
import tmidev.localaccount.domain.model.AppConfiguration
import tmidev.localaccount.domain.model.type.ThemeStyle
import javax.inject.Inject

/**
 * Repository interface for application preferences/configurations.
 */
interface AppPreferencesRepository {
    /**
     * Data stream of essential preferences.
     */
    val appConfigurationStream: Flow<AppConfiguration>

    /**
     * Toggles between using dynamic colors or not.
     */
    suspend fun toggleDynamicColors()

    /**
     * Change the app theme.
     *
     * @param themeStyle the [ThemeStyle] to be used.
     */
    suspend fun changeThemeStyle(themeStyle: ThemeStyle)

    /**
     * Checks if the user chooses to stay signed in.
     *
     * @return true if should reconnect, false otherwise.
     */
    suspend fun isStayConnectedEnabled(): Boolean

    /**
     * Set the current user id.
     *
     * @param userId [Int] value that represents the user id to be saved.
     * @param stayConnected [Boolean] value to indicate when this user should be reconnected.
     */
    suspend fun setCurrentUserId(userId: Int, stayConnected: Boolean)

    /**
     * Get the current user id.
     *
     * @return [Int] value from current user id or 0 if there is no id saved.
     */
    suspend fun getCurrentUserId(): Int
}

/**
 * Implementation of [AppPreferencesRepository] that uses [LocalAccountPreferences].
 */
class AppPreferencesRepositoryImpl @Inject constructor(
    private val localAccountPreferences: LocalAccountPreferences
) : AppPreferencesRepository {
    override val appConfigurationStream: Flow<AppConfiguration> =
        localAccountPreferences.appConfigurationStream

    override suspend fun toggleDynamicColors() =
        localAccountPreferences.toggleDynamicColors()

    override suspend fun changeThemeStyle(themeStyle: ThemeStyle) =
        localAccountPreferences.changeThemeStyle(themeStyle = themeStyle)

    override suspend fun isStayConnectedEnabled(): Boolean =
        localAccountPreferences.isStayConnectedEnabled()

    override suspend fun setCurrentUserId(userId: Int, stayConnected: Boolean) =
        localAccountPreferences.setCurrentUserId(userId = userId, stayConnected = stayConnected)

    override suspend fun getCurrentUserId(): Int =
        localAccountPreferences.getCurrentUserId()
}