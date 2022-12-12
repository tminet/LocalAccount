package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.AppPreferencesRepository
import javax.inject.Inject

/**
 * Use case to logout the current user.
 */
interface LogoutUseCase {
    suspend operator fun invoke()
}

/**
 * Implementation of [LogoutUseCase] that uses [AppPreferencesRepository].
 */
class LogoutUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) : LogoutUseCase {
    override suspend fun invoke() =
        appPreferencesRepository.setCurrentUserId(userId = 0, stayConnected = false)
}