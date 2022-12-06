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

class LocalAccountPreferences @Inject constructor(
    private val dataStorePreferences: DataStore<Preferences>
) {
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

    suspend fun toggleDynamicColors() {
        tryIt {
            dataStorePreferences.edit { preferences ->
                val current = preferences[PreferencesKeys.useDynamicColors] ?: true
                preferences[PreferencesKeys.useDynamicColors] = !current
            }
        }
    }

    suspend fun changeThemeStyle(themeStyle: ThemeStyle) {
        tryIt {
            dataStorePreferences.edit { preferences ->
                preferences[PreferencesKeys.themeStyle] = themeStyle.asString()
            }
        }
    }

    suspend fun isStayConnectedEnabled(): Boolean = dataStorePreferences.data
        .catch { exception ->
            exception.printStackTrace()
            emit(value = emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferencesKeys.stayConnected] ?: false
        }.first()

    suspend fun setCurrentUserId(userId: Int, stayConnected: Boolean) {
        tryIt {
            dataStorePreferences.edit { preferences ->
                preferences[PreferencesKeys.currentUserId] = userId
                preferences[PreferencesKeys.stayConnected] = stayConnected
            }
        }
    }

    suspend fun getCurrentUserId(): Int = dataStorePreferences.data
        .catch { exception ->
            exception.printStackTrace()
            emit(value = emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferencesKeys.currentUserId] ?: 0
        }.first()

    private suspend fun tryIt(action: suspend () -> Unit) {
        try {
            action()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    private fun ThemeStyle.asString(): String = this.name

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