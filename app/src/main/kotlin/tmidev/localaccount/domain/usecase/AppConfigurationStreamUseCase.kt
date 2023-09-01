package tmidev.localaccount.domain.usecase

import kotlinx.coroutines.flow.Flow
import tmidev.localaccount.data.repository.AppPreferencesRepository
import tmidev.localaccount.domain.model.AppConfiguration
import javax.inject.Inject

/**
 * Use case to get the data stream of essential preferences of application.
 */
interface AppConfigurationStreamUseCase {
    operator fun invoke(): Flow<AppConfiguration>
}

/**
 * Implementation of [AppConfigurationStreamUseCase] that uses [AppPreferencesRepository].
 */
class AppConfigurationStreamUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) : AppConfigurationStreamUseCase {
    override fun invoke(): Flow<AppConfiguration> =
        appPreferencesRepository.appConfigurationStream
}