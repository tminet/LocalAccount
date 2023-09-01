package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.AppPreferencesRepository
import javax.inject.Inject

/**
 * Use case to check if the user chooses to stay signed in.
 */
interface IsStayConnectedEnabledUseCase {
    /**
     * @return true if should reconnect, false otherwise.
     */
    suspend operator fun invoke(): Boolean
}

/**
 * Implementation of [IsStayConnectedEnabledUseCase] that uses [AppPreferencesRepository].
 */
class IsStayConnectedEnabledUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) : IsStayConnectedEnabledUseCase {
    override suspend fun invoke(): Boolean =
        appPreferencesRepository.isStayConnectedEnabled()
}