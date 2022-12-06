package tmidev.localaccount.data.repository

import kotlinx.coroutines.flow.Flow
import tmidev.localaccount.data.datasource.datastore.LocalAccountPreferences
import tmidev.localaccount.domain.model.AppConfiguration
import tmidev.localaccount.domain.model.type.ThemeStyle
import javax.inject.Inject

interface AppPreferencesRepository {
    val appConfigurationStream: Flow<AppConfiguration>

    suspend fun toggleDynamicColors()

    suspend fun changeThemeStyle(themeStyle: ThemeStyle)

    suspend fun isStayConnectedEnabled(): Boolean

    suspend fun setCurrentUserId(userId: Int, stayConnected: Boolean)

    suspend fun getCurrentUserId(): Int
}

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