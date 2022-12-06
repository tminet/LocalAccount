package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.AppPreferencesRepository
import javax.inject.Inject

interface LogoutUseCase {
    suspend operator fun invoke()
}

class LogoutUseCaseImpl @Inject constructor(
    private val irPreferencesRepository: AppPreferencesRepository
) : LogoutUseCase {
    override suspend fun invoke() =
        irPreferencesRepository.setCurrentUserId(userId = 0, stayConnected = false)
}