package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.AppPreferencesRepository
import tmidev.localaccount.domain.model.type.ThemeStyle
import javax.inject.Inject

interface ChangeThemeStyleUseCase {
    suspend operator fun invoke(themeStyle: ThemeStyle)
}

class ChangeThemeStyleUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) : ChangeThemeStyleUseCase {
    override suspend fun invoke(themeStyle: ThemeStyle) =
        appPreferencesRepository.changeThemeStyle(themeStyle = themeStyle)
}