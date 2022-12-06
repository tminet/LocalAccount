package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.AppPreferencesRepository
import javax.inject.Inject

interface GetCurrentUserIdUseCase {
    suspend operator fun invoke(): Int
}

class GetCurrentUserIdUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) : GetCurrentUserIdUseCase {
    override suspend fun invoke(): Int =
        appPreferencesRepository.getCurrentUserId()
}