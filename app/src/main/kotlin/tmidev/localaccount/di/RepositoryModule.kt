package tmidev.localaccount.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmidev.localaccount.data.repository.AppPreferencesRepository
import tmidev.localaccount.data.repository.AppPreferencesRepositoryImpl
import tmidev.localaccount.data.repository.UsersRepository
import tmidev.localaccount.data.repository.UsersRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindsAppPreferencesRepository(
        repository: AppPreferencesRepositoryImpl
    ): AppPreferencesRepository

    @Binds
    @Singleton
    fun bindsUsersRepository(
        repository: UsersRepositoryImpl
    ): UsersRepository
}