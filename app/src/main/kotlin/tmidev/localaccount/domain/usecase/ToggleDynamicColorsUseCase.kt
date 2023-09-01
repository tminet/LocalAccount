package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.AppPreferencesRepository
import javax.inject.Inject

/**
 * Use case to toggle between using dynamic colors or not.
 */
interface ToggleDynamicColorsUseCase {
    suspend operator fun invoke()
}

/**
 * Implementation of [ToggleDynamicColorsUseCase] that uses [AppPreferencesRepository].
 */
class ToggleDynamicColorsUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) : ToggleDynamicColorsUseCase {
    override suspend fun invoke() =
        appPreferencesRepository.toggleDynamicColors()
}