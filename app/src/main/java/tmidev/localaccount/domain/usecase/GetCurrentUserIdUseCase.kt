package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.AppPreferencesRepository
import javax.inject.Inject

/**
 * Use case to get the current logged user id.
 */
interface GetCurrentUserIdUseCase {
    /**
     * @return [Int] value of the user id that is logged. 0 represents that there is no
     * currently user logged.
     */
    suspend operator fun invoke(): Int
}

/**
 * Implementation of [GetCurrentUserIdUseCase] that uses [AppPreferencesRepository].
 */
class GetCurrentUserIdUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) : GetCurrentUserIdUseCase {
    override suspend fun invoke(): Int =
        appPreferencesRepository.getCurrentUserId()
}