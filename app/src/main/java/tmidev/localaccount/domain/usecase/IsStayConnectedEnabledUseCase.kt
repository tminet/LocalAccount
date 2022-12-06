package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.AppPreferencesRepository
import javax.inject.Inject

interface IsStayConnectedEnabledUseCase {
    suspend operator fun invoke(): Boolean
}

class IsStayConnectedEnabledUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) : IsStayConnectedEnabledUseCase {
    override suspend fun invoke(): Boolean =
        appPreferencesRepository.isStayConnectedEnabled()
}