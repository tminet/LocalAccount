package tmidev.localaccount.domain.usecase

import kotlinx.coroutines.flow.Flow
import tmidev.localaccount.data.repository.AppPreferencesRepository
import tmidev.localaccount.domain.model.AppConfiguration
import javax.inject.Inject

interface AppConfigurationStreamUseCase {
    operator fun invoke(): Flow<AppConfiguration>
}

class AppConfigurationStreamUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) : AppConfigurationStreamUseCase {
    override fun invoke(): Flow<AppConfiguration> =
        appPreferencesRepository.appConfigurationStream
}