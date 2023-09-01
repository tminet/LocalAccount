package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.AppPreferencesRepository
import tmidev.localaccount.domain.model.type.ThemeStyle
import javax.inject.Inject

/**
 * Use case to change the app theme style.
 */
interface ChangeThemeStyleUseCase {
    /**
     * @param themeStyle the [ThemeStyle] to be used.
     */
    suspend operator fun invoke(themeStyle: ThemeStyle)
}

/**
 * Implementation of [ChangeThemeStyleUseCase] that uses [AppPreferencesRepository].
 */
class ChangeThemeStyleUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) : ChangeThemeStyleUseCase {
    override suspend fun invoke(themeStyle: ThemeStyle) =
        appPreferencesRepository.changeThemeStyle(themeStyle = themeStyle)
}