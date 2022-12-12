package tmidev.localaccount.data.datasource.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import tmidev.localaccount.domain.model.AppConfiguration
import tmidev.localaccount.domain.model.type.ThemeStyle
import javax.inject.Inject

/**
 * Preferences for application. This class uses [DataStore Preferences][DataStore] to persist data.
 */
class LocalAccountPreferences @Inject constructor(
    private val dataStorePreferences: DataStore<Preferences>
) {
    /**
     * Data stream of essential preferences.
     */
    val appConfigurationStream: Flow<AppConfiguration> = dataStorePreferences.data
        .catch { exception ->
            exception.printStackTrace()
            emit(value = emptyPreferences())
        }
        .map { preferences ->
            val useDynamicColors = preferences[PreferencesKeys.useDynamicColors] ?: true
            val themeStyle = preferences[PreferencesKeys.themeStyle].asThemeStyle()

            AppConfiguration(
                useDynamicColors = useDynamicColors,
                themeStyle = themeStyle
            )
        }

    /**
     * Toggles between using dynamic colors or not.
     */
    suspend fun toggleDynamicColors() {
        tryIt {
            dataStorePreferences.edit { preferences ->
                val current = preferences[PreferencesKeys.useDynamicColors] ?: true
                preferences[PreferencesKeys.useDynamicColors] = !current
            }
        }
    }

    /**
     * Change the app theme.
     *
     * @param themeStyle the [ThemeStyle] to be used.
     */
    suspend fun changeThemeStyle(themeStyle: ThemeStyle) {
        tryIt {
            dataStorePreferences.edit { preferences ->
                preferences[PreferencesKeys.themeStyle] = themeStyle.asString()
            }
        }
    }

    /**
     * Checks if the user chooses to stay signed in.
     *
     * @return true if should reconnect, false otherwise.
     */
    suspend fun isStayConnectedEnabled(): Boolean = dataStorePreferences.data
        .catch { exception ->
            exception.printStackTrace()
            emit(value = emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferencesKeys.stayConnected] ?: false
        }.first()

    /**
     * Set the current user id.
     *
     * @param userId [Int] value that represents the user id to be saved.
     * @param stayConnected [Boolean] value to indicate when this user should be reconnected.
     */
    suspend fun setCurrentUserId(userId: Int, stayConnected: Boolean) {
        tryIt {
            dataStorePreferences.edit { preferences ->
                preferences[PreferencesKeys.currentUserId] = userId
                preferences[PreferencesKeys.stayConnected] = stayConnected
            }
        }
    }

    /**
     * Get the current user id.
     *
     * @return [Int] value from current user id or 0 if there is no id saved.
     */
    suspend fun getCurrentUserId(): Int = dataStorePreferences.data
        .catch { exception ->
            exception.printStackTrace()
            emit(value = emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferencesKeys.currentUserId] ?: 0
        }.first()

    /**
     * Extension to try/catch an suspend block.
     */
    private suspend fun tryIt(action: suspend () -> Unit) {
        try {
            action()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    /**
     * Extension to map [ThemeStyle] into [String].
     */
    private fun ThemeStyle.asString(): String = this.name

    /**
     * Extension to map [String] into [ThemeStyle].
     */
    private fun String?.asThemeStyle(): ThemeStyle = when (this) {
        ThemeStyle.LightMode.name -> ThemeStyle.LightMode
        ThemeStyle.DarkMode.name -> ThemeStyle.DarkMode
        else -> ThemeStyle.FollowAndroidSystem
    }

    private object PreferencesKeys {
        val useDynamicColors = booleanPreferencesKey(name = "use_dynamic_colors")
        val themeStyle = stringPreferencesKey(name = "theme_style")
        val stayConnected = booleanPreferencesKey(name = "stay_connected")
        val currentUserId = intPreferencesKey(name = "current_user_id")
    }
}