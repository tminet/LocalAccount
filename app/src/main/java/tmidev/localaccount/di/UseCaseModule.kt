package tmidev.localaccount.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import tmidev.localaccount.domain.usecase.AppConfigurationStreamUseCase
import tmidev.localaccount.domain.usecase.AppConfigurationStreamUseCaseImpl
import tmidev.localaccount.domain.usecase.ChangeThemeStyleUseCase
import tmidev.localaccount.domain.usecase.ChangeThemeStyleUseCaseImpl
import tmidev.localaccount.domain.usecase.GetCurrentUserIdUseCase
import tmidev.localaccount.domain.usecase.GetCurrentUserIdUseCaseImpl
import tmidev.localaccount.domain.usecase.GetUserByEmailUseCase
import tmidev.localaccount.domain.usecase.GetUserByEmailUseCaseImpl
import tmidev.localaccount.domain.usecase.GetUserByIdStreamUseCase
import tmidev.localaccount.domain.usecase.GetUserByIdStreamUseCaseImpl
import tmidev.localaccount.domain.usecase.GetUserByIdUseCase
import tmidev.localaccount.domain.usecase.GetUserByIdUseCaseImpl
import tmidev.localaccount.domain.usecase.InsertUserUseCase
import tmidev.localaccount.domain.usecase.InsertUserUseCaseImpl
import tmidev.localaccount.domain.usecase.IsEmailAvailableUseCase
import tmidev.localaccount.domain.usecase.IsEmailAvailableUseCaseImpl
import tmidev.localaccount.domain.usecase.IsStayConnectedEnabledUseCase
import tmidev.localaccount.domain.usecase.IsStayConnectedEnabledUseCaseImpl
import tmidev.localaccount.domain.usecase.LogoutUseCase
import tmidev.localaccount.domain.usecase.LogoutUseCaseImpl
import tmidev.localaccount.domain.usecase.SetCurrentUserIdUseCase
import tmidev.localaccount.domain.usecase.SetCurrentUserIdUseCaseImpl
import tmidev.localaccount.domain.usecase.ToggleDynamicColorsUseCase
import tmidev.localaccount.domain.usecase.ToggleDynamicColorsUseCaseImpl
import tmidev.localaccount.domain.usecase.UpdateUserUseCase
import tmidev.localaccount.domain.usecase.UpdateUserUseCaseImpl
import tmidev.localaccount.domain.usecase.ValidateEmailFieldUseCase
import tmidev.localaccount.domain.usecase.ValidateEmailFieldUseCaseImpl
import tmidev.localaccount.domain.usecase.ValidateSimpleFieldUseCase
import tmidev.localaccount.domain.usecase.ValidateSimpleFieldUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {
    @Binds
    @ViewModelScoped
    fun bindsAppConfigurationStreamUseCase(
        useCase: AppConfigurationStreamUseCaseImpl
    ): AppConfigurationStreamUseCase

    @Binds
    @ViewModelScoped
    fun bindsToggleDynamicColorsUseCase(
        useCase: ToggleDynamicColorsUseCaseImpl
    ): ToggleDynamicColorsUseCase

    @Binds
    @ViewModelScoped
    fun bindsChangeThemeStyleUseCase(
        useCase: ChangeThemeStyleUseCaseImpl
    ): ChangeThemeStyleUseCase

    @Binds
    @ViewModelScoped
    fun bindsIsStayConnectedEnabledUseCase(
        useCase: IsStayConnectedEnabledUseCaseImpl
    ): IsStayConnectedEnabledUseCase

    @Binds
    @ViewModelScoped
    fun bindsSetCurrentUserIdUseCase(
        useCase: SetCurrentUserIdUseCaseImpl
    ): SetCurrentUserIdUseCase

    @Binds
    @ViewModelScoped
    fun bindsGetCurrentUserIdUseCase(
        useCase: GetCurrentUserIdUseCaseImpl
    ): GetCurrentUserIdUseCase

    @Binds
    @ViewModelScoped
    fun bindsLogoutUseCase(
        useCase: LogoutUseCaseImpl
    ): LogoutUseCase

    @Binds
    @ViewModelScoped
    fun bindsGetUserByIdStreamUseCase(
        useCase: GetUserByIdStreamUseCaseImpl
    ): GetUserByIdStreamUseCase

    @Binds
    @ViewModelScoped
    fun bindsGetUserByIdUseCase(
        useCase: GetUserByIdUseCaseImpl
    ): GetUserByIdUseCase

    @Binds
    @ViewModelScoped
    fun bindsGetUserByEmailUseCase(
        useCase: GetUserByEmailUseCaseImpl
    ): GetUserByEmailUseCase

    @Binds
    @ViewModelScoped
    fun bindsIsEmailAvailableUseCase(
        useCase: IsEmailAvailableUseCaseImpl
    ): IsEmailAvailableUseCase

    @Binds
    @ViewModelScoped
    fun bindsInsertUserUseCase(
        useCase: InsertUserUseCaseImpl
    ): InsertUserUseCase

    @Binds
    @ViewModelScoped
    fun bindsUpdateUserUseCase(
        useCase: UpdateUserUseCaseImpl
    ): UpdateUserUseCase

    @Binds
    @ViewModelScoped
    fun bindsValidateSimpleFieldUseCase(
        useCase: ValidateSimpleFieldUseCaseImpl
    ): ValidateSimpleFieldUseCase

    @Binds
    @ViewModelScoped
    fun bindsValidateEmailFieldUseCase(
        useCase: ValidateEmailFieldUseCaseImpl
    ): ValidateEmailFieldUseCase
}