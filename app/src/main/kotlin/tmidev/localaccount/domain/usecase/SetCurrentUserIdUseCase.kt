package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.AppPreferencesRepository
import javax.inject.Inject

/**
 * Set the logged user id.
 */
interface SetCurrentUserIdUseCase {
    /**
     * @param userId [Int] value of user id to be saved.
     * @param stayConnected [Boolean] value to indicate when this user should be reconnected.
     */
    suspend operator fun invoke(userId: Int, stayConnected: Boolean)
}

/**
 * Implementation of [SetCurrentUserIdUseCase] that uses [AppPreferencesRepository].
 */
class SetCurrentUserIdUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) : SetCurrentUserIdUseCase {
    override suspend fun invoke(userId: Int, stayConnected: Boolean) =
        appPreferencesRepository.setCurrentUserId(userId = userId, stayConnected = stayConnected)
}