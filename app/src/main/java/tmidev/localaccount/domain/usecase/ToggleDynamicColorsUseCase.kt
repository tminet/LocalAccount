package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.AppPreferencesRepository
import javax.inject.Inject

interface ToggleDynamicColorsUseCase {
    suspend operator fun invoke()
}

class ToggleDynamicColorsUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) : ToggleDynamicColorsUseCase {
    override suspend fun invoke() =
        appPreferencesRepository.toggleDynamicColors()
}